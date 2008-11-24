package com.googlecode.mibible.printer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.googlecode.mibible.printer.filter.TextNumFilter;
import com.googlecode.mibible.printer.filter.ShortTextualOidFilter;
import com.googlecode.mibible.printer.filter.ShortNumericOidFilter;
import com.googlecode.mibible.printer.filter.LongNumericOidFilter;
import com.googlecode.mibible.printer.filter.LongTextualOidFilter;
import com.googlecode.mibible.printer.filter.PrintFilter;
import com.googlecode.mibible.printer.filter.TypeFilter;

import net.percederberg.mibble.Mib;
import net.percederberg.mibble.MibLoader;
import net.percederberg.mibble.MibLoaderException;
import net.percederberg.mibble.MibSymbol;
import net.percederberg.mibble.MibValue;
import net.percederberg.mibble.MibValueSymbol;
import net.percederberg.mibble.value.ObjectIdentifierValue;

/**
 * MIBファイルをパースし、標準出力に表示する。
 * 
 * @author snuffkin
 * @since 0.2.0
 */
public class MibPrinter
{
    /** ヘルプ文字列 */
    private static final String COMMAND_HELP
        = "Usage: mibprinter <format> <file or URL>\n";
    
    /** 正しい引数の数を表す定数 */
    private static final int NUM_OF_ARGS = 2;
    
    /** 引数「出力フォーマット」の位置を表す定数 */
    private static final int ARGS_FORMAT = 0;
    /** 引数「file or URL」の位置を表す定数 */
    private static final int ARGS_TARGET = 1;
    
    /**
     * 指定されたフォーマットで、指定されたMIBファイルを出力する。
     * @param args {フォーマット, MIBファイル}
     */
    public static void main(String[] args)
    {
        // 引数が少ないときはHELPを出力して終了する
        if (args.length != NUM_OF_ARGS)
        {
            System.out.println(COMMAND_HELP);
            System.exit(1);
        }
        
        // 引数に指定された「出力フォーマット」を取得する
        String format = args[ARGS_FORMAT];
        // 引数に指定された「file or URL」を取得する
        String target = args[ARGS_TARGET];
        
        // MIB出力クラス
        MibPrinter printer = null;
        try
        {
            // MIBを読み込む
            try
            {
                // URLからMIBを読み込む
                URL url = new URL(target);
                printer = new MibPrinter(url);
            }
            catch (MalformedURLException exception)
            {
                // ファイルからMIBを読み込む
                printer = new MibPrinter(new File(target));
            }
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
            System.exit(1);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.exit(1);
        }
        catch (MibLoaderException ex)
        {
            ex.printStackTrace();
            System.exit(1);
        }
        catch (RuntimeException ex)
        {
            ex.printStackTrace();
            System.exit(1);
        }

        // MIB Treeを出力する
        printer.printMib(format);
    }
    
    ////////////////////　以下、クラス本体　////////////////////
    
    /** 読み込んだMIB */
    private Mib mib;
    /** MIBのrootオブジェクト */
    private ObjectIdentifierValue rootOid;
    /** MIBのrootオブジェクト */
    private List<PrintFilter> filterList = new ArrayList<PrintFilter>();
    
    /**
     * パースするために必要なMIBファイルを読み込んで、保持する。
     * 
     * @param file パースするMIBファイル
     * @throws IOException　指定したファイルを読み込めない場合に発生する
     * @throws MibLoaderException 指定したMIBファイルがパースできない場合に発生する
     */
    public MibPrinter(File file) throws IOException, MibLoaderException
    {
        MibLoader loader = new MibLoader();
        loader.addDir(file.getParentFile());
        this.mib = loader.load(file);
        this.rootOid = getRootOid(this.mib);
        
        // TODO いずれ高速化対応が必要
        this.filterList.add(new TextNumFilter());
        this.filterList.add(new ShortTextualOidFilter());
        this.filterList.add(new ShortNumericOidFilter());
        this.filterList.add(new LongNumericOidFilter());
        this.filterList.add(new LongTextualOidFilter());
        this.filterList.add(new TypeFilter());
    }
    
    /**
     * パースするために必要なMIBファイルを読み込んで、保持する。
     * 
     * @param url パースするMIBファイルを示すURL
     * @throws IOException　指定したファイルを読み込めない場合に発生する
     * @throws MibLoaderException 指定したMIBファイルがパースできない場合に発生する
     */
    public MibPrinter(URL url) throws IOException, MibLoaderException
    {
        MibLoader loader = new MibLoader();
        this.mib = loader.load(url);
        this.rootOid = getRootOid(this.mib);
        
        // TODO いずれ高速化対応が必要
        this.filterList.add(new TextNumFilter());
        this.filterList.add(new ShortTextualOidFilter());
        this.filterList.add(new ShortNumericOidFilter());
        this.filterList.add(new LongNumericOidFilter());
        this.filterList.add(new LongTextualOidFilter());
        this.filterList.add(new TypeFilter());
    }
    
    /**
     * 指定されたMIB情報のrootのOIDオブジェクトを取得する。
     * 
     * @param mib rootのOIDオブジェクトを取得したいMIB情報
     * @return rootのOIDオブジェクト
     */
    private static ObjectIdentifierValue getRootOid(Mib mib)
    {
        ObjectIdentifierValue root = null;

        // 最初に見つかったOID情報を取得する
        Iterator iter = mib.getAllSymbols().iterator();
        while (root == null && iter.hasNext())
        {
            MibSymbol symbol = (MibSymbol) iter.next();
            if (symbol instanceof MibValueSymbol)
            {
                MibValue value = ((MibValueSymbol) symbol).getValue();
                if (value instanceof ObjectIdentifierValue)
                {
                    root = (ObjectIdentifierValue) value;
                }
            }
        }
        if (root == null)
        {
            // OIDがない場合はroot oidはnull
            System.out.println("no OID value");
            return null;
        }

        // 取得したOIDから上位にたどり、rootのOIDを取得する
        while (root.getParent() != null)
        {
            root = root.getParent();
        }
        return root;
    }
    
    /**
     * 指定した出力フォーマットで、MIB情報を表示する。
     * 
     * @param format 出力フォーマット
     */
    public void printMib(String format)
    {
        printOid(this.rootOid, format);
    }
    
    /**
     * 指定されたOIDを指定されたフォーマットで出力する。
     * 
     * @param oid 出力するOID
     * @param format 出力フォーマット
     */
    private void printOid(ObjectIdentifierValue oid, String format)
    {
        String output = format;
        for (PrintFilter filter : this.filterList)
        {
            String key = filter.getFilterKey();
            // 適用すべきPrintFilterかどうか、出力フォーマットを確認する
            if (format.contains(key))
            {
                // PrintFilterを適用する
                String printString = filter.getPrintString(oid);
                output = output.replace(key, printString);
            }
        }
        
        // PrintFilterを適用後のMIB情報を出力する
        System.out.println(output);
        
        // 子OIDに対して、再帰的にし出力処理を実行する
        for (int i = 0; i < oid.getChildCount(); i++)
        {
            printOid(oid.getChild(i), format);
        }
    }
}
