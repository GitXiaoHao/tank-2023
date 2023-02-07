package top.yh.utils;

import java.io.*;

/**
 * @author yuhao
 * @date 2023/2/4
 **/
public class SerialCloneable implements Cloneable, Serializable
{

    private static final long serialVersionUID = 1L;
    /**深拷贝*/
//    public Object clone()
//{
//    try
//    {
//        // save the object to a byte array
//        //将该对象序列化成流,因为写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面
//        ByteArrayOutputStream bout = new ByteArrayOutputStream();
//        ObjectOutputStream out = new ObjectOutputStream(bout);
//        out.writeObject(this);
//        out.close();
//
//        // read a clone of the object from the byte array
//        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
//        ObjectInputStream in = new ObjectInputStream(bin);
//        Object ret = in.readObject();
//        in.close();
//
//        return ret;
//    }
//    catch (Exception e)
//    {
//        e.printStackTrace();
//        return null;
//    }
//}

    @Override
    public Object clone(){
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}

