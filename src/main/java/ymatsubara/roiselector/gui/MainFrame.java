package ymatsubara.roiselector.gui;

import ymatsubara.roiselector.common.Config;
import ymatsubara.roiselector.util.FileUtil;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends BaseInitializer implements ActionListener, ListSelectionListener, MouseListener, MouseMotionListener
{
    private int sx, sy, ex, ey;
    private List<String> rectList;
    private ImageIcon imgIcon;

    private MainFrame() {
        super();
        this.save.addActionListener(this);
        this.undo.addActionListener(this);
        this.copy.addActionListener(this);
        this.paste.addActionListener(this);
        this.moveUp.addActionListener(this);
        this.moveDown.addActionListener(this);
        this.inputFileList.addListSelectionListener(this);
        this.sx = -1;
        this.sy = -1;
        this.ex = -1;
        this.ey = -1;
        this.rectList = new ArrayList<>();
        this.imgIcon = null;
    }

    // ActionListener method
    public void actionPerformed(ActionEvent ae) {
        List<String> list;
        switch (ae.getActionCommand()) {
            case Config.SAVE_LABEL:
                FileUtil.writePositiveFile(this.rectMap);
                this.saveLabel.setText("");
                break;

            case Config.UNDO_LABEL:
                list = this.rectMap.get(this.currentImgName);
                if (list.size() > 0) {
                    list.remove(list.size() - 1);
                    this.posCount--;
                    if (list.size() > 0) {
                        this.rectMap.put(this.currentImgName, list);
                    } else {
                        this.rectMap.remove(this.currentImgName);
                        this.undo.setEnabled(false);
                    }

                    System.out.println("Undo successfully " + String.valueOf(list.size()));
                    this.statusLabel.setText(String.valueOf(list.size()) + " mark(s) recorded here");
                    this.saveLabel.setText("*****");
                    this.etcLabel.setText(String.valueOf(this.posCount) + " mark(s) in total");
                    this.imagePanel.removeAll();
                    this.imagePanel.add(this.imageLabel, BorderLayout.CENTER);
                    this.imagePanel.setVisible(false);
                    this.imagePanel.setVisible(true);
                }
                break;

            case Config.COPY_LABEL:
                System.out.println(Config.COPY_LABEL);
                this.copyFlag = true;
                this.copyList = new ArrayList<>();
                this.paste.setEnabled(true);
                break;

            case Config.PASTE_LABEL:
                System.out.println(Config.PASTE_LABEL);
                this.copyFlag = false;
                list = new ArrayList<>();
                if (this.rectMap.containsKey(this.currentImgName)) {
                    list = this.rectMap.get(this.currentImgName);
                }

                if (this.copyList.size() > 0) {
                    for (int i = 0; i < this.copyList.size(); i++) {
                        list.add(this.copyList.get(i));
                    }

                    this.posCount += this.copyList.size();
                    this.statusLabel.setText(String.valueOf(list.size()) + " mark(s) recorded here");
                    this.etcLabel.setText(String.valueOf(this.posCount) + " mark(s) in total");
                    this.rectMap.put(this.currentImgName, list);
                }

                this.undo.setEnabled(true);
                break;

            case Config.MOVE_UP_LABEL:
                System.out.println(Config.MOVE_UP_LABEL);
                this.inputFileList.setSelectedIndex(this.currentIndex - 1);
                this.inputFileList.ensureIndexIsVisible(this.inputFileList.getSelectedIndex());
                break;

            case Config.MOVE_DOWN_LABEL:
                System.out.println(Config.MOVE_DOWN_LABEL);
                this.inputFileList.setSelectedIndex(this.currentIndex + 1);
                this.inputFileList.ensureIndexIsVisible(this.inputFileList.getSelectedIndex());
                break;
        }
    }

    // MouseListener methods
    public void mouseClicked(MouseEvent me){}

    public void mouseEntered(MouseEvent me) {
        if (this.rectMap.containsKey(this.currentImgName)) {
            this.rectList = this.rectMap.get(this.currentImgName);
            Graphics g = this.imageLabel.getGraphics();
            g.setColor(Color.red);
            for (int i = 0; i < this.rectList.size(); i++) {
                String[] elements = this.rectList.get(i).split(Config.DELIMITER);
                g.drawRect(Integer.parseInt(elements[0]), Integer.parseInt(elements[1]),
                        Integer.parseInt(elements[2]), Integer.parseInt(elements[3]));
            }
            g.dispose();
        }
    }

    public void mouseExited(MouseEvent me){}

    public void mousePressed(MouseEvent me) {
        this.sx = me.getX();
        this.sy = me.getY();
        this.statusLabel.setText("S: (" + String.valueOf(this.sx) + "," + String.valueOf(this.sy) + ")");
        Graphics g = this.imageLabel.getGraphics();
        g.setColor(Color.red);
        g.drawOval(this.sx, this.sy, 5, 5);
    }

    public void mouseReleased(MouseEvent me) {
        this.ex = me.getX();
        this.ey = me.getY();
        if (this.ex < 0) {
            this.ex = 0;
        } else if (this.ex >= this.imgIcon.getIconWidth()) {
            this.ex = this.imgIcon.getIconWidth() - 1;
        }

        if (this.ey < 0) {
            this.ey = 0;
        } else if (this.ey >= this.imgIcon.getIconHeight()) {
            this.ey = this.imgIcon.getIconHeight() - 1;
        }

        if (this.ex < this.sx) {
            int tmp = this.sx;
            this.sx = this.ex;
            this.ex = tmp;
        }

        if (this.ey < this.sy) {
            int tmp = this.sy;
            this.sy = this.ey;
            this.ey = tmp;
        }

        int width = this.ex - this.sx;
        int height = this.ey - this.sy;
        if (width > 0 && height > 0) {
            Graphics g = this.imageLabel.getGraphics();
            g.setColor(Color.red);
            g.drawRect(this.sx, this.sy, width, height);
            g.dispose();
            String rectStr = String.valueOf(this.sx) + Config.DELIMITER + String.valueOf(this.sy) + Config.DELIMITER
                    + String.valueOf(width) + Config.DELIMITER + String.valueOf(height);
            this.rectList.add(rectStr);
            this.rectMap.put(this.currentImgName, this.rectList);
            this.posCount++;
            String line = "S: (" + String.valueOf(this.sx) + "," + String.valueOf(this.sy) + ")  E: ("
                    + String.valueOf(this.ex) + "," + String.valueOf(this.ey) + ")    Size: " + String.valueOf(width * height);
            this.statusLabel.setText(line);
            this.saveLabel.setText("*****");
            this.etcLabel.setText(String.valueOf(this.posCount) + " mark(s) in total.");
            this.undo.setEnabled(true);
            if (this.copyFlag) {
                this.copyList.add(rectStr);
            }
        }
    }

    // MouseMotionListener methods
    public void mouseDragged(MouseEvent me) {}

    public void mouseMoved(MouseEvent me) {
        if (this.rectMap.containsKey(this.currentImgName)) {
            this.rectList = this.rectMap.get(this.currentImgName);
            Graphics g = this.imageLabel.getGraphics();
            g.setColor(Color.red);
            for (int i = 0;i < this.rectList.size(); i++) {
                String[] elements = this.rectList.get(i).split(Config.DELIMITER);
                g.drawRect(Integer.parseInt(elements[0]), Integer.parseInt(elements[1]),
                        Integer.parseInt(elements[2]), Integer.parseInt(elements[3]));
            }
            g.dispose();
        }
    }

    // ListSelectionListener method
    public void valueChanged(ListSelectionEvent lse) {
        this.currentIndex = this.inputFileList.getSelectedIndex();
        this.currentImgName = this.inputFiles[this.currentIndex].toString();
        this.imgIcon = new ImageIcon(this.inputFiles[this.currentIndex].toString());
        this.imageLabel = new JLabel(this.imgIcon);
        this.imageLabel.setPreferredSize(new Dimension(this.imgIcon.getIconWidth(), this.imgIcon.getIconHeight()));
        if (!this.rectMap.containsKey(this.currentImgName)) {
            this.undo.setEnabled(false);
            this.rectList = new ArrayList<>();
            this.statusLabel.setText("No mark recorded here");
        } else if (this.rectMap.get(this.currentImgName).size() > 0) {
            this.undo.setEnabled(true);
            this.statusLabel.setText(String.valueOf(this.rectMap.get(this.currentImgName).size()) + " mark(s) recorded here");
        } else {
            this.statusLabel.setText("No mark recorded here");
        }

        this.imageLabel.addMouseListener(this);
        this.imageLabel.addMouseMotionListener(this);
        this.imagePanel.removeAll();
        this.imagePanel.add(this.imageLabel);
        this.imagePanel.setVisible(false);
        this.imagePanel.setVisible(true);
    }

    public static void main(String[] args) {
        MainFrame rs = new MainFrame();
    }
}