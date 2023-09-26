package top.yh.utils;

import java.io.*;

/**
 * @author yuhao
 * @date 2023/2/4
 **/
public class SerialCloneable<T> implements Cloneable, Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;
    @Override
    public T clone(){
        try {
            return (T) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}

