package com.googlecode.mibible.printer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.googlecode.mibible.printer.filter.DetailStringFilter;
import com.googlecode.mibible.printer.filter.ObjectNameFilter;
import com.googlecode.mibible.printer.filter.ObjectValueFilter;
import com.googlecode.mibible.printer.filter.OidNumberFilter;
import com.googlecode.mibible.printer.filter.OidStringFilter;
import com.googlecode.mibible.printer.filter.PrintFilter;

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
        printer.printMibTree(format);
	}
	
	////////////////////　以下、クラス本体　////////////////////
	
	/** 読み込んだMIB */
	private Mib mib;
	/** MIBのrootオブジェクト */
	private ObjectIdentifierValue rootOid;
	/** MIBのrootオブジェクト */
	private List<PrintFilter> filterList = new ArrayList<PrintFilter>();
	
	public MibPrinter(File file) throws IOException, MibLoaderException
	{
		MibLoader loader = new MibLoader();
        loader.addDir(file.getParentFile());
        this.mib = loader.load(file);
        this.rootOid = getRootOid(this.mib);
        
        // TOOD 高速化
        this.filterList.add(new DetailStringFilter());
        this.filterList.add(new ObjectNameFilter());
        this.filterList.add(new ObjectValueFilter());
        this.filterList.add(new OidNumberFilter());
        this.filterList.add(new OidStringFilter());
	}
	
	public MibPrinter(URL url) throws IOException, MibLoaderException
	{
		MibLoader loader = new MibLoader();
        this.mib = loader.load(url);
        this.rootOid = getRootOid(this.mib);
        
        // TOOD 高速化
        this.filterList.add(new DetailStringFilter());
        this.filterList.add(new ObjectNameFilter());
        this.filterList.add(new ObjectValueFilter());
        this.filterList.add(new OidNumberFilter());
        this.filterList.add(new OidStringFilter());
	}
	
	private static ObjectIdentifierValue getRootOid(Mib mib)
	{
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
            return null;
        }

        while (root.getParent() != null)
        {
            root = root.getParent();
        }
        return root;
	}
	
	public void printMibTree(String format)
	{
        printOid(this.rootOid, format);
	}
	
    /**
     * Prints the detailed OID tree starting in the specified OID. 
     *
     * @param oid            the OID node to print
     */
    private void printOid(ObjectIdentifierValue oid, String format)
    {
    	String output = format;
    	for (PrintFilter filter : this.filterList)
    	{
    		String key = filter.getFilterKey();
    		if (format.contains(key))
    		{
    			String printString = filter.getPrintString(oid);
    			output = output.replace(key, printString);
    		}
    	}
        System.out.println(output);
        for (int i = 0; i < oid.getChildCount(); i++)
        {
            printOid(oid.getChild(i), format);
        }
    }
}
