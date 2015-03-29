package com.granveaud.mysql2h2converter.converter;

import com.granveaud.mysql2h2converter.parser.ParseException;
import com.granveaud.mysql2h2converter.parser.SQLParserManager;
import com.granveaud.mysql2h2converter.sql.Statement;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

public class Main {
    final static private Charset INPUT_CHARSET = Charset.forName("UTF-8");

    public static void main(String[] args) throws IOException, ParseException {
        if (args.length < 1) {
            System.err.println("Usage: input_file1 input_file2...");
            System.exit(1);
        }

        for (String str : args) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(readFile(str), INPUT_CHARSET));
            Iterator<Statement> sourceIterator = SQLParserManager.parseScript(reader);

            // conversion and execution
            Iterator<Statement> it = H2Converter.convertScript(sourceIterator);
            while (it.hasNext()) {
                Statement st = it.next();

                // output to stdout
                System.out.println(st.toString() + ";");
            }
        }

    }

    public static InputStream readFile(String name) throws IOException {
        InputStream input = new BufferedInputStream(new FileInputStream(name));
        PushbackInputStream pb = new PushbackInputStream( input, 2 );
        byte [] magicbytes = new byte[2];
        pb.read(magicbytes);
        pb.unread(magicbytes);
        ByteBuffer bb = ByteBuffer.wrap(magicbytes);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        short magic = bb.getShort();
        if( magic == (short)GZIPInputStream.GZIP_MAGIC )
            return new GZIPInputStream( pb );
        else
            return pb;
    }
}
