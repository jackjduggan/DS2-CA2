package com.hbjdjd.ca2;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.NoTypePermission;

import java.io.*;

public class Storage
{
    public static Object readObject(File file) throws IOException, ClassNotFoundException {
        String fileName = file.getPath();
        Object object;
        XStream xstream = new XStream(new DomDriver());
        xstream.addPermission(NoTypePermission.NONE);//Suppress warning, found on stackoverflow
        xstream.allowTypesByRegExp(new String[]{".*"});//Suppress warning
        ObjectInputStream in = xstream.createObjectInputStream(new FileReader(fileName));
        object = in.readObject();
        in.close();
        return object;
    }

    public static void writeObject(File file, Object o) throws IOException
    {
        String fileName = file.getPath();
        XStream xstream = new XStream(new DomDriver());
        xstream.addPermission(NoTypePermission.NONE);//Suppress warning
        xstream.allowTypesByRegExp(new String[]{".*"});//Suppress warning
        ObjectOutputStream out = xstream.createObjectOutputStream(new FileWriter(fileName));
        out.writeObject(o);
        out.flush();
        out.close();
    }

//    public static Object readObject(File file) throws IOException, ClassNotFoundException {
//        String fileName = file.getPath();
//        Object object;
//        FileInputStream in = new FileInputStream(fileName);
//        ObjectInputStream ois = new ObjectInputStream(in);
//        object = ois.readObject();
//        ois.close();
//        return object;
//    }
//
//    public static void writeObject(File file, Object o) throws IOException
//    {
//        String fileName = file.getPath();
//        FileOutputStream out = new FileOutputStream(fileName);
//        ObjectOutputStream oos = new ObjectOutputStream(out);
//        oos.writeObject(o);
//        out.flush();
//        out.close();
//    }


}
