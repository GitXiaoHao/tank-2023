package top.yh.listen;

import lombok.Getter;
import top.yh.resources.GameCommonData;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * @author yuhao
 * @date 2023/2/4
 * 监听一些数据的改变
 **/

public class SomeListenValue {
    /**
     * 共击败了多少坦克
     */
    @Getter
    private Integer killNumber;

    public SomeListenValue(Integer killNumber) {
        this.killNumber = killNumber;
    }

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);


    public void setKillNumber(Integer newValue) {
        Integer oldValue = this.killNumber;
        this.killNumber = newValue;
        this.pcs.firePropertyChange(GameCommonData.killNumberName, oldValue, newValue);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
}
