package ymatsubara.roiselector.gui;

import ymatsubara.roiselector.common.Config;
import ymatsubara.roiselector.util.FileUtil;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class BaseInitializer extends JFrame {
    protected boolean copyFlag;
    protected int posCount, currentIndex;
    protected String currentImgName;
    protected TreeMap<String, List<String>> rectMap;
    protected List<String> copyList;
    protected File[] inputFiles;
    protected JMenuBar menuBar;
    protected JMenu operatorMenu;
    protected JMenuItem save, undo, copy, paste, moveUp, moveDown;
    protected JPanel imagePanel, statusBar;
    protected JLabel statusLabel, saveLabel, etcLabel, imageLabel;
    protected JList<String> inputFileList;

    protected BaseInitializer() {
        initVariables();
        createFrame();
        createMenuBar();
        createImagePanel();
        createFileListPane();
        createStatusBar();
    }

    protected void initVariables() {
        this.copyFlag = false;
        this.posCount = 0;
        this.currentIndex = 0;
        this.currentImgName = "";
        this.rectMap = new TreeMap<>();
        this.copyList = new ArrayList<>();
        File posFile = new File(Config.OUTPUT_POSITIVE_FILE);
        if (posFile.exists()) {
            this.rectMap = FileUtil.readPositiveFile(posFile);
            for (String key : this.rectMap.keySet()) {
                this.posCount += this.rectMap.get(key).size();
            }
        }
        FileUtil.writeNegativeFile();
    }

    protected void createFrame() {
        this.setTitle(Config.FRAME_TITLE);
        this.getContentPane().setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(Config.FRAME_WIDTH, Config.FRAME_HEIGHT));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setResizable(true);
    }

    protected void createMenuBar() {
        this.menuBar = new JMenuBar();
        this.operatorMenu = new JMenu(Config.OPERATION_LABEL);
        this.save = new JMenuItem(Config.SAVE_LABEL);
        this.undo = new JMenuItem(Config.UNDO_LABEL);
        this.copy = new JMenuItem(Config.COPY_LABEL);
        this.paste = new JMenuItem(Config.PASTE_LABEL);
        this.moveUp = new JMenuItem(Config.MOVE_UP_LABEL);
        this.moveDown= new JMenuItem(Config.MOVE_DOWN_LABEL);
        this.save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        this.undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        this.copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
        this.paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
        this.moveUp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK));
        this.moveDown.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
        this.save.setEnabled(true);
        this.undo.setEnabled(false);
        this.copy.setEnabled(true);
        this.paste.setEnabled(false);
        this.operatorMenu.add(this.save);
        this.operatorMenu.add(this.undo);
        this.operatorMenu.add(this.copy);
        this.operatorMenu.add(this.paste);
        this.operatorMenu.add(this.moveUp);
        this.operatorMenu.add(this.moveDown);
        this.menuBar.add(this.operatorMenu);
        this.setJMenuBar(this.menuBar);
    }

    protected void createImagePanel() {
        this.imagePanel = new JPanel();
        this.imagePanel.setBackground(Color.gray);
        this.add(this.imagePanel, BorderLayout.CENTER);
    }

    protected void createFileListPane() {
        this.inputFiles = FileUtil.getInputFiles();
        String[] inputFileNames = FileUtil.getFileNames(inputFiles);
        this.inputFileList = new JList<>(inputFileNames);
        this.inputFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane fileListPane = new JScrollPane();
        fileListPane.getViewport().setView(this.inputFileList);
        fileListPane.setPreferredSize(new Dimension(Config.SCROLL_PANE_WIDTH, Config.SCROLL_PANE_HEIGHT));
        this.add(fileListPane, BorderLayout.LINE_START);
    }

    protected void createStatusBar() {
        this.statusBar = new JPanel();
        this.statusBar.setLayout(new GridLayout(1, 5));
        this.statusBar.setBackground(Color.white);
        this.statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
        this.statusLabel = new JLabel("Loaded image files");
        this.saveLabel = new JLabel("");
        this.etcLabel = new JLabel(String.valueOf(this.posCount) + " mark(s) in total");
        this.statusBar.add(this.saveLabel);
        this.statusBar.add(this.statusLabel);
        this.statusBar.add(new JLabel(""));
        this.statusBar.add(this.etcLabel);
        this.statusBar.add(new JLabel(""));
        this.add(this.statusBar, BorderLayout.PAGE_END);
        this.setVisible(true);
    }
}
