package chess.view;

import chess.controller.Controller;
import chess.controller.Type;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class View {
    private Controller controller;
    private JFrame frame;
    private JLabel[] labels;
    private JLabel[] labelsOverlay;
    private JPanel panel;
    private JPanel boardPanel;
    private JPanel aboveBoardPanel;
    private Type type;


    public View(Controller controller) {
        this.controller = controller;

        frame = createFrame();
        panel = createPanel();

        boardPanel = createBoardPanel();
        labels = createSpaceLabels();
        aboveBoardPanel = createAboveBoardPanel();

        labelsOverlay = new JLabel[64];
        for (int i = 0; i < 64; i++) {
            labelsOverlay[i] = new JLabel();
            labelsOverlay[i].setOpaque(false);
            aboveBoardPanel.add(labelsOverlay[i]);
        }

        panel.add(aboveBoardPanel);
        panel.add(boardPanel);
        frame.add(panel, BorderLayout.CENTER);
    }

    public void show() {
        frame.setVisible(true);
    }

    private JFrame createFrame() {
        JFrame f = new JFrame();

        f.setLayout(new BorderLayout(0, 0));
        f.setTitle("Chess");
        f.setSize(800, 800);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        f.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                int newWidth = labels[0].getWidth();
                int newHeight = labels[0].getHeight();

                controller.rescaleIcons(newWidth, newHeight);
            }
        });

        return f;
    }

    private JPanel createPanel() {
        JPanel p = new JPanel();

        p.setLayout(new OverlayLayout(p));

        return p;
    }

    private JLabel[] createSpaceLabels() {
        JLabel[] l = new JLabel[64];

        for (int i = 0; i < 64; i++) {
            l[i] = new JLabel();

            l[i].setOpaque(true);
            boardPanel.add(l[i]);
        }

        return l;
    }

    private JPanel createBoardPanel() {
        JPanel bp = new JPanel();

        bp.setLayout(new GridLayout(8, 8, 1, 1));
        bp.setVisible(true);

        return bp;
    }

    private void setSelectedLabel(MouseEvent e) {
        int x = -1;
        int y = -1;

        //int testX = e.getX() / (labels[0].getWidth() + 1); // 1 is gap
        //int testY = e.getY() / (labels[0].getHeight() + 1); // 1 is gap

        for (int i = 0; i < 8; i++)
            if (e.getX() < labels[i].getX() + labels[i].getWidth()) {
                x = i;
                break;
            }

        if (x != -1)
            for (int i = 0; i < 8; i++)
                if (e.getY() < labels[i * 8].getY() + labels[i * 8].getHeight()) {
                    y = i;
                    controller.setSelectedPiece(x, y);
                    break;
                }
    }

    private void setSelectedCoordinates (MouseEvent e) {
        int x = e.getX() - labelsOverlay[0].getWidth() / 2;
        int y = e.getY() - labelsOverlay[0].getHeight() / 2;

        controller.setSelectedPieceCoordinates(x, y);
    }

    private JPanel createAboveBoardPanel() {
        JPanel abp = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (controller.isAPieceSelected())
                    g.drawImage(controller.getSelectedPieceImage(), controller.getSelectedPieceDragX(), controller.getSelectedPieceDragY(), this);
            }
        };

        abp.setLayout(new GridLayout(8, 8, 1, 1));
        abp.setVisible(true);
        abp.setOpaque(false);
        abp.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setSelectedLabel(e);
                setSelectedCoordinates(e);

                abp.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                controller.mouseReleased(e.getX() / labelsOverlay[0].getWidth(), e.getY() / labelsOverlay[0].getHeight());

                abp.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        abp.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                setSelectedCoordinates(e);

                abp.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });

        return abp;
    }

    public void setBackground(int i, Color color) {
        labels[i].setBackground(color);
    }

    public void setIcon(int i, ImageIcon icon) {
        labels[i].setIcon(icon);
    }
}
