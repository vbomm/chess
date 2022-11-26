package chess.view;

import chess.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Sends relevant user actions to the Controller.
 * Receives orders from the Controller.
 */
public class View {
    private Controller controller;
    private JFrame frame;
    private JLabel[] labels;
    private JLabel[] labelsOverlay;
    private JPanel panel;
    private JPanel boardPanel;
    private JPanel aboveBoardPanel;


    /**
     * Default constructor.
     * Creates the visuals for the User and sends the user input to the Controller.
     *
     * @param controller the Controller this View communicates with
     */
    public View(Controller controller) {
        this.controller = controller;

        frame = createFrame();
        panel = createPanel();

        boardPanel = createBoardPanel();
        labels = createTileLabels();
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

    /**
     * Makes the frame visible.
     */
    public void show() {
        frame.setVisible(true);
    }

    /**
     * Creates and returns a JFrame
     *
     * @return created JFrame
     */
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

    /**
     * Creates and returns a JPanel.
     *
     * @return created JPanel
     */
    private JPanel createPanel() {
        JPanel p = new JPanel();

        p.setLayout(new OverlayLayout(p));

        return p;
    }

    /**
     * Creates and returns an array of JLabel. Each JLabel represents a tile of the board.
     *
     * @return array of JLabel
     */
    private JLabel[] createTileLabels() {
        JLabel[] l = new JLabel[64];

        for (int i = 0; i < 64; i++) {
            l[i] = new JLabel();

            l[i].setOpaque(true);
            boardPanel.add(l[i]);
        }

        return l;
    }

    /**
     * Creates and returns a JPanel for the chess board.
     *
     * @return created JPanel
     */
    private JPanel createBoardPanel() {
        JPanel bp = new JPanel();

        bp.setLayout(new GridLayout(8, 8, 1, 1));
        bp.setVisible(true);

        return bp;
    }

    /**
     * Checks which label was clicked and if valid, sends the coordinates to the Controller.
     *
     * @param e the MouseEvent
     */
    private void setSelectedLabel(MouseEvent e) {
        int x = -1;
        int y;

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

    /**
     * Sends the coordinates where the selected piece should be displayed.
     *
     * @param e the MouseEvent
     */
    private void setSelectedCoordinates (MouseEvent e) {
        int x = e.getX() - labelsOverlay[0].getWidth() / 2;
        int y = e.getY() - labelsOverlay[0].getHeight() / 2;

        controller.setSelectedPieceCoordinates(x, y);
    }

    /**
     * Creates and returns a JPanel. It is used to display the selected piece.
     * Adds Listeners for the Mouse.
     *
     * @return the created JPanel
     */
    private JPanel createAboveBoardPanel() {
        JPanel abp = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (controller.isAPieceSelected())
                    g.drawImage(controller.getSelectedPieceIcon(), controller.getSelectedPieceDragX(), controller.getSelectedPieceDragY(), this);
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
                controller.deselectPiece(e.getX() / labelsOverlay[0].getWidth(), e.getY() / labelsOverlay[0].getHeight());

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

    /**
     * Sets the background of a tile.
     *
     * @param i     the index of the tile
     * @param color the new color of the tile
     */
    public void setBackground(int i, Color color) {
        labels[i].setBackground(color);
    }

    /**
     * Sets the icon of a tile.
     *
     * @param i    the index of the tile
     * @param icon the new icon of the tile
     */
    public void setIcon(int i, ImageIcon icon) {
        labels[i].setIcon(icon);
    }
}
