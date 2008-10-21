package com.googlecode.mibible.printer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import net.percederberg.mibble.Mib;
import net.percederberg.mibble.MibLoader;
import net.percederberg.mibble.MibLoaderException;
import net.percederberg.mibble.MibSymbol;
import net.percederberg.mibble.MibValue;
import net.percederberg.mibble.MibValueSymbol;
import net.percederberg.mibble.value.ObjectIdentifierValue;

/**
 * MIBファイルをパースし、標準出力に表示します。
 * 
 * @author snuffkin
 * @since 0.2.0
 */
public class MibPrinter
{

	/** ヘルプ文字列 */
	private static final String COMMAND_HELP
	    = "Usage: mibprinter <format> <file or URL>\n";
	
	/**  */
	private static final int MIN_ARGS = 2;
	
	/**  */
	private static final int ARGS_FORMAT = 0;
	/**  */
	private static final int ARGS_TARGET = 1;
	
	/**  */
	private static final String OPTION_OID_NUMBER = "%on";
	/**  */
	private static final String OPTION_OID_STRING = "%os";
	/**  */
	private static final String OPTION_NUMBER = "%n";
	/**  */
	private static final String OPTION_STRING = "%s";
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// 引数が少ないときはHELPを出力して終了する
		if (args.length != MIN_ARGS)
		{
			System.out.println(COMMAND_HELP);
			System.exit(1);
		}
		
		// 引数に指定された「出力フォーマット」を取得する
		String format = args[ARGS_FORMAT];
		// 引数に指定された「file or URL」を取得する
		String target = args[ARGS_TARGET];
		// MIBを読む込むクラス
		MibLoader  loader = new MibLoader();
		
        Mib  mib = null; // 読み込んだMIBオブジェクト
        URL  url;        // 読み込むURL
        File file;       // 読み込むファイル
        
        // 引数に指定した文字列がURLかファイル名か判定する
        try
        {
            url = new URL(target);
        }
        catch (MalformedURLException exception)
        {
            url = null;
        }
        
        // MIBを読み込む
        try
        {
            if (url == null)
            {
                // ファイルからMIBを読み込む
                file = new File(target);
                loader.addDir(file.getParentFile());
                mib = loader.load(file);
            }
            else
            {
                // URLからMIBを読み込む
                mib = loader.load(url);
            }
            
            // warningが発生した場合、エラー出力する
            if (mib.getLog().warningCount() > 0)
            {
                mib.getLog().printTo(System.err);
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
        MibPrinter printer = new MibPrinter(loader);
        printer.printMibTree(format);
	}
	
	/** MIBを読む込むクラス */
	MibLoader  loader = new MibLoader();
	
	public MibPrinter(MibLoader loader)
	{
		this.loader = loader;
	}
	public void printMibTree(String format)
	{
		Mib[] mibs = this.loader.getAllMibs();
		
		// エラーチェック
        if (mibs.length <= 0)
        {
            System.out.println("no MIB data");
            return;
        }
        
        Mib mib = loader.getAllMibs()[0];
        Iterator iter = mib.getAllSymbols().iterator();
        ObjectIdentifierValue  root = null;
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
        	System.out.println("no OID value");
            return;
        }

        while (root.getParent() != null)
        {
            root = root.getParent();
        }
        printOid(root);
	}
	
    /**
     * Prints the detailed OID tree starting in the specified OID. 
     *
     * @param oid            the OID node to print
     */
    private static void printOid(ObjectIdentifierValue oid)
    {
        System.out.println(oid.getName());
//        System.out.println(oid.toDetailString());
        for (int i = 0; i < oid.getChildCount(); i++)
        {
            printOid(oid.getChild(i));
        }
    }
}
