package top.yh.resources;

import lombok.Getter;
import lombok.Setter;
import top.yh.utils.GetImage;
import top.yh.utils.GetProperties;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * @author yuhao
 * @date 2023/2/2
 **/
@Getter
public abstract class ViewAbstract {

    protected final String filePath;
    /**
     * 窗口大小
     */

    protected int windowsWidth;
    protected int windowsHeight;
    /**
     * 背景图
     */

    protected ImageIcon backGroundImage;
    /**
     * 登录窗口图标
     */

    protected Image loginWindowsImage;
    /**
     * 窗口标题
     */
    protected String windowsTitle;
    /**
     * 存放视图数据的map
     * key是数据名
     * value是数据
     */
    protected Map<String, String> viewDataMap;
    protected int size = 0;
    protected int menuWindowX;
    protected int menuWindowY;
    protected int buttonX;
    protected int buttonY;
    protected int buttonWidth;
    protected int buttonHeight;
    protected int buttonDistance;
    protected int labelWidth;
    protected int labelHeight;
    /**
     * 双缓冲图片
     */
    @Setter
    protected Image image = null;
    /**
     * 失败背景图
     */
    protected Image failBackGroundImage;
    protected int flushMinus;
    /**
     * 胜利背景图
     */
    protected Image winBackGroundImage;

    /**
     * 构造方法
     * @param filePath 读取的文件
     */
    public ViewAbstract(String filePath) {
        //文件名
        this.filePath = filePath;
        //先初始化公有数据
        this.initPropertiesData();
        this.otherInitData();
    }

    /**
     * 初始化数据
     */
    private void initPropertiesData() {
        //map数据
        viewDataMap = GetProperties.getAllProperties(filePath);
        windowsWidth = Integer.parseInt(viewDataMap.get("windowsWidth"));
        windowsHeight = Integer.parseInt(viewDataMap.get("windowsHeight"));
        backGroundImage = GetImage.getImageIcon(viewDataMap.get("backGroundImage"));
        loginWindowsImage = GetImage.getImage(viewDataMap.get("loginWindowsImage"));
        windowsTitle = viewDataMap.get("windowsTitle");
    }

    /**
     * 初始化其他的数据 由继承者进行编写
     */
    public void otherInitData(){

    }

    /**
     * 初始化面板
     */
    public JPanel getJpanel() {
        //面板
        JPanel panel = new JPanel();
        //设置大小
        panel.setBounds(0, 0, getWindowsWidth(), getWindowsHeight());
        //布局管理器
        panel.setLayout(null);
        //设置面板背景
        panel.setBackground(Color.WHITE);
        return panel;
    }
    protected JPanel addButton(JPanel panel){
        return panel;
    }

    protected void initButton(JButton button) {

    }
    /**
     * 初始化JFrame
     *
     * @param jFrame 窗体
     */
    public void initFrame(JFrame jFrame) {
        //设置标题
        jFrame.setTitle(getWindowsTitle());
        //设置大小
        jFrame.setSize(getWindowsWidth(), getWindowsHeight());
        //不可改变大小
        jFrame.setResizable(false);
        //窗口在屏幕中间
        jFrame.setLocationRelativeTo(null);
        //设置退出键
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //窗口图标
        jFrame.setIconImage(getLoginWindowsImage());
        //添加面板
        jFrame.getContentPane().add(addBackImage());
    }


    public JPanel initOtherPanel(JPanel panel) {
        return panel;
    }

    /**
     * 一定得放最后 不然会出问题！
     * 添加背景
     */
    public JPanel addBackImage() {
        JPanel panel = initOtherPanel(getJpanel());
        JLabel label = new JLabel();
        label.setBounds(0, 0, getWindowsWidth(), getWindowsHeight());
        //图片
        label.setIcon(getBackGroundImage());
        //加入面板
        panel.add(label);
        return panel;
    }
}
