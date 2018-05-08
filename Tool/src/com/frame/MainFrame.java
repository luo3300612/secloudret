package com.frame;

import com.ellipseCurve.Steps;
import com.utils.FIleUtils;
import com.utils.FileCipherUtil;
import com.utils.FileCompressUtil;
import com.utils.Utils;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.theme.SubstanceTerracottaTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class MainFrame extends JFrame {
    private JPanel p1;
    private JPanel p2;
    private JPanel p3;
    private JPanel p4;


    private JButton fileChooseButton;
    private JButton encryptButton;
    private JButton outputChooseButton;
    private JButton decryptButton;
    private JButton dfileChooseButton;
    private JButton doutputChooseButton;
    private JButton keyPathChooseButton;
    private JButton keyGenButton;
    private JButton p2KeyPathChooseButton;
    private JButton DButton;
    private JButton SButton;
    private JButton doorGenButton;
    private JButton p3KeyPathChooseButton;
    private JButton p4KeyPathChooseButton;
    private JButton p4OutPathChoose;
    private JButton pubPathChooseButton;
    private JButton shareEncryptButton;

    private JLabel encryptPathLb;
    private JLabel outputPathLb;
    private JLabel dencryptPathLb;
    private JLabel doutputPathLb;
    private JLabel[] kwdLb;
    private JTextField[] kwdText;
    private JLabel[] searchKwLb;
    private JTextField[] searchKwText;
    private JLabel[] notKwLb;
    private JTextField[] notKwText;
    private JLabel keyPathShowLb;
    private JLabel p2KeyPathShowLb;
    private JLabel p3KeyPathShowLb;
    private JLabel p4KeyPathShowLb;
    private JLabel p4OutPathShowLb;
    private JLabel pubShowLb;


    private JTextField outputFileName;

    private int mode;
    private int[] indexOfFiles;
    private int inputkeywordnum;
    private int inputnotwordnum;
    private String[] keywords;

    public MainFrame() {
        setResizable(false);
        setTitle("SeCloudret工具箱");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImage(
                Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/imgs/log.png"))
        );
        setBounds(200, 200, 500, 500);

        JTabbedPane tab = new JTabbedPane(JTabbedPane.TOP);

        Container container = this.getLayeredPane();

        JPanel combop = new JPanel();
        p1 = new JPanel();
        p2 = new JPanel();
        p3 = new JPanel();
        p4 = new JPanel();

        tab.add(p1, "秘钥生成");
        tab.add(p2, "文件加密");
        tab.add(p3, "文件解密");
        tab.add(p4, "关键词预查询");

        combop.add(new JLabel("SeCloudret工具箱"));


        container.setLayout(new BorderLayout());
        container.add(tab, BorderLayout.CENTER);


        int buttonWidth = 90;
        int buttonHeight = 30;
        int labelWidth = 200;
        int labelHeight = 40;
        int x_left = 125;
        int x_right = 225;
        int x_title = 205;
        int p1_yst = 70;
        Font font = new Font("黑体", Font.PLAIN, 20);

        //p1
        p1.setLayout(null);
        JLabel p1TextLabel = new JLabel("秘钥生成");
        p1TextLabel.setBounds(x_title + 5, p1_yst + 20, 200, 25);
        p1TextLabel.setFont(font);
        p1.add(p1TextLabel);

        keyPathChooseButton = new JButton("生成到");
        keyPathShowLb = new JLabel();
        keyGenButton = new JButton("生成");

        keyPathChooseButton.setBounds(x_left-30, p1_yst + 74, buttonWidth - 20, buttonHeight);
        p1.add(keyPathChooseButton);

        keyPathShowLb.setBounds(x_right - 35, p1_yst + 69, 300, labelHeight);
        p1.add(keyPathShowLb);

        keyGenButton.setBounds(x_title + 10, p1_yst + 124, buttonWidth - 20, buttonHeight);
        p1.add(keyGenButton);


        //p2
        p2.setLayout(null);


        JLabel p2TextLabel = new JLabel("文件加密");
        p2TextLabel.setBounds(x_title + 5, 20, 200, 25);
        p2TextLabel.setFont(font);
        p2.add(p2TextLabel);


        fileChooseButton = new JButton("选择文件");
        fileChooseButton.setBounds(x_left - 40, 74, buttonWidth, buttonHeight);
        p2.add(fileChooseButton);

        outputChooseButton = new JButton("输出目录");
        outputChooseButton.setBounds(x_left - 40, 114, buttonWidth, buttonHeight);
        p2.add(outputChooseButton);

        p2KeyPathChooseButton = new JButton("秘钥路径");
        p2KeyPathChooseButton.setBounds(x_left - 40, 154, buttonWidth, buttonHeight);
        p2.add(p2KeyPathChooseButton);

        encryptButton = new JButton("加密");
        encryptButton.setBounds(x_left+20, 390, buttonWidth, buttonHeight);
        p2.add(encryptButton);

        outputFileName = new JTextField();

        encryptPathLb = new JLabel("请选择加密文件目录");
        encryptPathLb.setBounds(x_right - 40, 70, 300, labelHeight);
        p2.add(encryptPathLb);

        outputPathLb = new JLabel("请选择输出文件目录");
        outputPathLb.setBounds(x_right - 40, 110, 300, labelHeight);
        p2.add(outputPathLb);

        p2KeyPathShowLb = new JLabel(keyPathShowLb.getText());
        p2KeyPathShowLb.setBounds(x_right - 40, 150, 300, labelHeight);
        p2.add(p2KeyPathShowLb);

        kwdLb = new JLabel[5];
        kwdText = new JTextField[5];

        int y_start = 194;
        int y_step = 40;
        for (int i = 0; i < 5; i++) {
            kwdLb[i] = new JLabel("关键词" + (i + 1));
            kwdText[i] = new JTextField();
            if (i % 2 == 0) {
                kwdLb[i].setBounds(x_left - 80, y_start + i / 2 * y_step, 200, 30);
                kwdText[i].setBounds(x_right - 110, y_start + i / 2 * y_step, 110, 30);
            } else {
                kwdLb[i].setBounds(x_left + 120, y_start + i / 2 * y_step, 200, 30);
                kwdText[i].setBounds(x_right + 90, y_start + i / 2 * y_step, 110, 30);
            }
            p2.add(kwdLb[i]);
            p2.add(kwdText[i]);
        }
        pubPathChooseButton = new JButton("公钥目录");
        pubPathChooseButton.setBounds(x_left - 40, 335, buttonWidth, buttonHeight);
        p2.add(pubPathChooseButton);

        pubShowLb = new JLabel("文件分享时选择");
        pubShowLb.setBounds(x_right - 40, 335, 300, buttonHeight);
        p2.add(pubShowLb);

        shareEncryptButton = new JButton("分享加密");
        shareEncryptButton.setBounds(x_right+20, 390, buttonWidth, buttonHeight);
        p2.add(shareEncryptButton);
        //p3
        int p3LabelHeight = 40;
        p3.setLayout(null);
        int p3yst = 70;
        JLabel p3TextLabel = new JLabel("文件解密");
        p3TextLabel.setBounds(x_title + 5, 20 + p3yst, 200, 25);
        p3TextLabel.setFont(font);
        p3.add(p3TextLabel);
        dfileChooseButton = new JButton("选择文件");
        dfileChooseButton.setBounds(x_left - 40, 74 + p3yst, buttonWidth, buttonHeight);
        p3.add(dfileChooseButton);

        doutputChooseButton = new JButton("输出目录");
        doutputChooseButton.setBounds(x_left - 40, 114 + p3yst, buttonWidth, buttonHeight);
        p3.add(doutputChooseButton);

        p3KeyPathChooseButton = new JButton("秘钥目录");
        p3KeyPathChooseButton.setBounds(x_left - 40, 154 + p3yst, buttonWidth, buttonHeight);
        p3.add(p3KeyPathChooseButton);

        dencryptPathLb = new JLabel("请选择加密文件");
        dencryptPathLb.setBounds(x_right - 40, 74 + p3yst, 300, p3LabelHeight);
        p3.add(dencryptPathLb);

        doutputPathLb = new JLabel("请选择输出文件目录");
        doutputPathLb.setBounds(x_right - 40, 114 + p3yst, 300, p3LabelHeight);
        p3.add(doutputPathLb);

        p3KeyPathShowLb = new JLabel("请选择秘钥位置");
        p3KeyPathShowLb.setBounds(x_right - 40, 150 + p3yst, 300, p3LabelHeight);
        p3.add(p3KeyPathShowLb);

        decryptButton = new JButton("解密");
        decryptButton.setBounds(x_title, 200 + p3yst, buttonWidth, buttonHeight);
        p3.add(decryptButton);

        //p4
        Font font2 = new Font("宋体", Font.PLAIN, 14);
        int p4y_start = 70;
        p4.setLayout(null);
        JLabel p4TextLabel = new JLabel("关键词查询");
        p4TextLabel.setBounds(x_title + 5, 20, 200, 25);
        p4TextLabel.setFont(font);
        p4.add(p4TextLabel);

        int kw_left = 60;
        int kw_right = 110;
        int not_left = 270;
        int not_right = 330;
        searchKwLb = new JLabel[5];
        searchKwText = new JTextField[5];
        for (int i = 0; i < 5; i++) {
            searchKwLb[i] = new JLabel("关键词" + (i + 1));
            searchKwText[i] = new JTextField();
            searchKwLb[i].setBounds(kw_left, p4y_start + i * y_step, 200, 30);
            searchKwText[i].setBounds(kw_right, p4y_start + i * y_step, 110, 30);
            p4.add(searchKwLb[i]);
            p4.add(searchKwText[i]);
        }
        notKwLb = new JLabel[5];
        notKwText = new JTextField[5];
        for (int i = 0; i < 5; i++) {
            notKwLb[i] = new JLabel("不含词" + (i + 1));
            notKwText[i] = new JTextField();
            notKwLb[i].setBounds(not_left, p4y_start + i * y_step, 200, 30);
            notKwText[i].setBounds(not_right, p4y_start + i * y_step, 110, 30);
            p4.add(notKwLb[i]);
            p4.add(notKwText[i]);
        }

        p4KeyPathChooseButton = new JButton("秘钥路径");
        p4KeyPathChooseButton.setBounds(x_left - 40, 280, buttonWidth, buttonHeight);
        p4.add(p4KeyPathChooseButton);

        p4KeyPathShowLb = new JLabel("请选择秘钥路径");
        p4KeyPathShowLb.setBounds(x_right - 40, 275, 300, 40);
        p4.add(p4KeyPathShowLb);

        p4OutPathChoose = new JButton("输出路径");
        p4OutPathChoose.setBounds(x_left - 40, 320, buttonWidth, buttonHeight);
        p4.add(p4OutPathChoose);

        p4OutPathShowLb = new JLabel("请选择输出路径");
        p4OutPathShowLb.setBounds(x_right - 40, 315, 300, 40);
        p4.add(p4OutPathShowLb);


        SButton = new JButton("搜索陷门");
        SButton.setBounds(x_left - 40, 360, buttonWidth, buttonHeight);
        SButton.setFont(font2);
        p4.add(SButton);

        DButton = new JButton("解密陷门");
        DButton.setBounds(x_left + 80, 360, buttonWidth, buttonHeight);
        DButton.setFont(font2);
        p4.add(DButton);

        doorGenButton = new JButton("陷门生成");
        doorGenButton.setBounds(x_left + 200, 360, buttonWidth, buttonHeight);
        doorGenButton.setFont(font2);
        p4.add(doorGenButton);
        addListerner();

    }

    private File fileChooseBox() {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.showDialog(new JLabel(), "选择");
        return jfc.getSelectedFile();
    }

    private File directoryChooseBox() {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.showDialog(new JLabel(), "选择");
        return jfc.getSelectedFile();
    }

    private void addListerner() {
        keyPathChooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyPathShowLb.setText(directoryChooseBox().getAbsolutePath());
            }
        });
        p2KeyPathChooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p2KeyPathShowLb.setText(directoryChooseBox().getAbsolutePath());
            }
        });
        p3KeyPathChooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p3KeyPathShowLb.setText(directoryChooseBox().getAbsolutePath());
            }
        });
        keyGenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path = keyPathShowLb.getText();
                if (Steps.keyGen(path)) {
                    p2KeyPathShowLb.setText(path);
                    p3KeyPathShowLb.setText(path);
                    JOptionPane.showMessageDialog(MainFrame.this, "生成成功!");
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this, "生成失败!");
                }
            }
        });
        pubPathChooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pubShowLb.setText(fileChooseBox().getAbsolutePath());
            }
        });
        shareEncryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pubPath = pubShowLb.getText();
                String[] keywords = new String[6];
                for (int i = 1; i <= 5; i++) {
                    keywords[i] = kwdText[i - 1].getText();
                }
                if (Steps.wholeEncrypt(encryptPathLb.getText(), outputPathLb.getText(), p2KeyPathShowLb.getText(), keywords,pubPath)) {
                    JOptionPane.showMessageDialog(MainFrame.this, "加密成功!");
                }
                System.out.println("加密成功");
            }
        });
        fileChooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encryptPathLb.setText(fileChooseBox().getAbsolutePath());
                outputPathLb.setText(Utils.parentDir(encryptPathLb.getText()));
            }
        });
        outputChooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputPathLb.setText(directoryChooseBox().getAbsolutePath());
            }
        });
        dfileChooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dencryptPathLb.setText(fileChooseBox().getAbsolutePath());
                doutputPathLb.setText(Utils.parentDir(dencryptPathLb.getText()) + "/out");
            }
        });
        doutputChooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doutputPathLb.setText(directoryChooseBox().getAbsolutePath());
            }
        });
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 先压缩文件
                // 对文件加密
                // temp.getAbsolutePath()
                String[] keywords = new String[6];
                for (int i = 1; i <= 5; i++) {
                    keywords[i] = kwdText[i - 1].getText();
                }
                if (Steps.wholeEncrypt(encryptPathLb.getText(), outputPathLb.getText(), p2KeyPathShowLb.getText(), keywords)) {
                    JOptionPane.showMessageDialog(MainFrame.this, "加密成功!");
                }
                System.out.println("加密成功");
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //File temp = new File(UUID.randomUUID().toString() + ".zip");
                //temp.deleteOnExit();
                try {
                    Steps.wholeDecrypt(dencryptPathLb.getText(), doutputPathLb.getText(), p3KeyPathShowLb.getText());
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (GeneralSecurityException e1) {
                    e1.printStackTrace();
                }
                //temp.delete();
                JOptionPane.showMessageDialog(MainFrame.this, "解密成功!");
                System.out.println("解密成功");
            }
        });
        p4KeyPathChooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p4KeyPathShowLb.setText(directoryChooseBox().getAbsolutePath());
            }
        });
        p4OutPathChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p4OutPathShowLb.setText(directoryChooseBox().getAbsolutePath());
            }
        });
        SButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputkeywordnum = 0;
                inputnotwordnum = 0;
                for (int i = 0; i < 5; i++) {
                    if (searchKwText[i].getText().length() != 0) {
                        inputkeywordnum += 1;
                    } else {
                        break;
                    }
                }
                for (int i = 0; i < 5; i++) {
                    if (notKwText[i].getText().length() != 0) {
                        inputnotwordnum += 1;
                    } else {
                        break;
                    }
                }
                keywords = new String[inputkeywordnum + 1];
                String[] notwords = new String[inputnotwordnum + 1];
                for (int i = 1; i <= inputkeywordnum; i++) {
                    keywords[i] = searchKwText[i - 1].getText();
                }
                for (int i = 1; i <= inputnotwordnum; i++) {
                    notwords[i] = notKwText[i - 1].getText();
                }
                //System.out.println(Arrays.toString(keywords));
                //System.out.println(Arrays.toString(notwords));
                mode = 1;
                try {
                    indexOfFiles = Steps.beforeSearch(keywords, notwords, p4KeyPathShowLb.getText());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                JOptionPane.showMessageDialog(MainFrame.this, "共有" + String.valueOf(indexOfFiles[0]) + "条结果！");
            }
        });

        DButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputkeywordnum = 0;
                inputnotwordnum = 0;
                for (int i = 0; i < 5; i++) {
                    if (searchKwText[i].getText().length() != 0) {
                        inputkeywordnum += 1;
                    } else {
                        break;
                    }
                }
                //System.out.println("DB:inputkeywordnum " + inputkeywordnum);
                for (int i = 0; i < 5; i++) {
                    if (notKwText[i].getText().length() != 0) {
                        inputnotwordnum += 1;
                    } else {
                        break;
                    }
                }
                keywords = new String[inputkeywordnum + 1];
                String[] notwords = new String[inputnotwordnum + 1];
                for (int i = 1; i <= inputkeywordnum; i++) {
                    keywords[i] = searchKwText[i - 1].getText();
                }
                for (int i = 1; i <= inputnotwordnum; i++) {
                    notwords[i] = notKwText[i - 1].getText();
                }
                //System.out.println(Arrays.toString(keywords));
                //System.out.println(Arrays.toString(notwords));
                mode = 2;
                try {
                    indexOfFiles = Steps.beforeSearch(keywords, notwords, p4KeyPathShowLb.getText());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                JOptionPane.showMessageDialog(MainFrame.this, "共有" + String.valueOf(indexOfFiles[0]) + "条结果！");
            }
        });
        doorGenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ArrayList<String> filepaths = new ArrayList<>();
                    Properties files = Utils.getProperties(p4KeyPathShowLb.getText() + "/files.properties");
                    File target = new File(p4OutPathShowLb.getText() + "/doortemp");
                    if (!target.exists()) {
                        target.mkdirs();
                    } else {
                        FIleUtils.delete(p4OutPathShowLb.getText() + "/doortemp");
                        target.mkdirs();
                    }
                    for (int i = 1; i <= indexOfFiles[0]; i++) {
                        int num = indexOfFiles[i];
                        int[] I = new int[6];
                        //System.out.println("inputkeywordnum:" + inputkeywordnum);
                        for (int j = 1; j <= 5; j++) {
                            I[j] = j;
                        }
                        String[] keywords = new String[6];
                        for (int j = 1; j <= 5; j++) {
                            keywords[j] = files.getProperty("word" + num + j);
                        }
                        //System.out.println("I" + Arrays.toString(I));

                        Steps.Trapdoor(p4KeyPathShowLb.getText(), keywords, I, i, mode, p4OutPathShowLb.getText() + "/doortemp");
                        filepaths.add(p4OutPathShowLb.getText() + "/doortemp/" + i + ".properties");
                    }
                    FileCompressUtil.mutileFileToGzip(p4OutPathShowLb.getText() + "/trapdoors.zip", filepaths);
                    FIleUtils.delete(p4OutPathShowLb.getText() + "/doortemp");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        /*try {
            UIManager.setLookAndFeel(new SubstanceLookAndFeel());
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            SubstanceLookAndFeel.setCurrentTheme(new SubstanceTerracottaTheme());
//          SubstanceLookAndFeel.setSkin(new EmeraldDuskSkin());
//          SubstanceLookAndFeel.setCurrentButtonShaper(new ClassicButtonShaper());
//          SubstanceLookAndFeel.setCurrentWatermark(new SubstanceBubblesWatermark());
//          SubstanceLookAndFeel.setCurrentBorderPainter(new StandardBorderPainter());
//            SubstanceLookAndFeel.setCurrentGradientPainter(new StandardGradientPainter());
//            SubstanceLookAndFeel.setCurrentTitlePainter(new FlatTitePainter());
        } catch (Exception e) {
            System.err.println("Something went wrong!");
        }*/
        MainFrame frame = new MainFrame();
        frame.setVisible(true);
    }
}
