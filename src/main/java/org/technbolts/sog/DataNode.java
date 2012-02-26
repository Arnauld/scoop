package org.technbolts.sog;

import java.util.Date;

public interface DataNode {

    boolean isSet(String path);

    void unset(String path);

    // ~~~~~~~~~~~~~~~~~~~~~~~~Getter~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    boolean getBoolean(String path);

    double getDouble(String path);

    float getFloat(String path);

    int getInt(String path);

    long getLong(String path);

    byte[] getBytes(String path);

    Date getDate(String path);

    String getString(String path);

    // ~~~~~~~~~~~~~~~~~~~~~~~~Setter~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    void setBoolean(String path, boolean value);

    void setDouble(String path, double value);

    void setFloat(String path, float value);

    void setInt(String path, int value);

    void setLong(String path, long value);

    void setBytes(String path, byte[] value);

    void setDate(String path, Date value);

    void setString(String path, String value);

    // ~~~~~~~~~~~~~~~~~~~~~~~~List of elements~~~~~~~~~~~~~~~~~~~~~~

    boolean isSet(String path, int index);

    void unset(String path, int index);

    int getCount(String path);

    // ~~~~~~~~~~~~~~~~~~~~~~~~Getter: List of elements~~~~~~~~~~~~~~

    boolean getBoolean(String path, int index);

    double getDouble(String path, int index);

    float getFloat(String path, int index);

    int getInt(String path, int index);

    long getLong(String path, int index);

    byte[] getBytes(String path, int index);

    Date getDate(String path, int index);

    String getString(String path, int index);

    // ~~~~~~~~~~~~~~~~~~~~~~~~Setter: List of elements~~~~~~~~~~~~~~

    void setBoolean(String path, int index, boolean value);

    void setDouble(String path, int index, double value);

    void setFloat(String path, int index, float value);

    void setInt(String path, int index, int value);

    void setLong(String path, int index, long value);

    void setBytes(String path, int index, byte[] value);

    void setDate(String path, int index, Date value);

    void setString(String path, int index, String value);
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~SubNode~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Create a new <strong>detached</strong> {@link DataNode}.
     * {@link DataNode} needs to be attached to another {@link DataNode} afterwards.
     * 
     * <strong>Be aware that</strong> a node should be attached to a node that belongs
     * to the tree that creates it.
     * 
     * @param localName
     * @return
     */
    DataNode createDataNode(String localName);

    DataNode getSubNode(String path);

    DataNode getSubNode(String path, int index);

    void setSubNode(String path, DataNode value);
    
    void setSubNode(String path, int index, DataNode value);

}
