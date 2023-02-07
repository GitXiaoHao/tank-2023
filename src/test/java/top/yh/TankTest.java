package top.yh;

import org.junit.Test;
import top.yh.obj.Super;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * @author yuhao
 * @date 2023/2/4
 **/
public class TankTest {
    @Test
    public void test(){
        Super.HeroBullet heroBullet = new Super.HeroBullet("../tank/heroBullet.properties");
        Super.HeroBullet heroBullet1 = (Super.HeroBullet) heroBullet.clone();
        System.out.println(heroBullet1);
        System.out.println(heroBullet1 == heroBullet);
        Integer size = 0;
        PropertyChangeSupport support = new PropertyChangeSupport(size);
        support.addPropertyChangeListener(evt -> System.out.println("change"));
        support.firePropertyChange("size",0, 0);
        size++;
    }
    @Test
    public void test2(){
        TestBean test = new TestBean();
        test.setStr("ddddd");
        test.addPropertyChangeListener(new PropertyChangeListener1());
        test.setStr("yyyy");
    }
    @Test
    public void test3(){
        //AbstractTankData data =new Super.EnemyTankBullet(PropertiesName.ENEMY_TANK_BULLET_PATH,null);
        //System.out.println(data instanceof Super.HeroBullet);
    }
    class TestBean {

        protected final PropertyChangeSupport support = new PropertyChangeSupport(this);

        private String str;

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            support.firePropertyChange("str", this.str, str);
            this.str = str;
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            support.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            support.removePropertyChangeListener(listener);
        }
    }

     class PropertyChangeListener1 implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            System.out.println( "1: " + evt.getPropertyName() + "  " + evt.getNewValue() + "  " + evt.getOldValue());

        }
    }

     class PropertyChangeListener2 implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            System.out.println( "2: " + evt.getPropertyName() + "  " + evt.getNewValue() + "  " + evt.getOldValue());

        }
    }
}
