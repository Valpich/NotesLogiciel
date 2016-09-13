package eseo.parser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import java.awt.Label;

public class Fenetre extends JFrame {
    private static final long     serialVersionUID = 8916731521037453201L;
    private final static String[] NOM_UE           = { "Modélisation et persistance des données",
                                                           "Modélisation et spécification des systèmes",
                                                           "Systèmes informatiques",
                                                           "Réseaux",
                                                           "Electronique Hautes Fréquences et Hyperfréquences",
                                                           "Transmissions RF",
                                                           "Traitement numérique du signal et de l'image",
                                                           "Automatique", "Microcontrôleurs",
                                                           "Communication", "Séminaire entreprise", "Anglais",
                                                           "Stage Découverte de l'Entreprise" };
    private JTable                table;
    private DefaultTableModel     tableModele;
    private JTextField            txtCookie;
    private String                ID;
    private String                nom;

    @SuppressWarnings( "unchecked" )
    public Fenetre() {
        setID( null );
        setTitle( "Notes ESEO" );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setVisible( true );
        setSize( new Dimension( 450, 400 ) );
        getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.X_AXIS ) );

        JPanel panel = new JPanel();
        getContentPane().add( panel );
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 225, 225, 0 };
        gbl_panel.rowHeights = new int[] { 0, 0, 92, 0 };
        gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gbl_panel.rowWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
        panel.setLayout( gbl_panel );

        txtCookie = new JTextField();
        txtCookie.addFocusListener( new FocusAdapter() {
            @Override
            public void focusGained( FocusEvent e ) {
                if ( txtCookie.getText().equals( "Saisir le cookie" ) )
                    txtCookie.setText( "" );
            }

            @Override
            public void focusLost( FocusEvent e ) {
                if ( txtCookie.getText().equals( "" ) )
                    txtCookie.setText( "Saisir le cookie" );
            }
        } );
        txtCookie.setToolTipText( "Cookie" );
        txtCookie.setHorizontalAlignment( SwingConstants.CENTER );
        txtCookie.setText( "Saisir le cookie" );
        GridBagConstraints gbc_txtCookie = new GridBagConstraints();
        gbc_txtCookie.fill = GridBagConstraints.BOTH;
        gbc_txtCookie.insets = new Insets( 0, 0, 5, 5 );
        gbc_txtCookie.gridx = 0;
        gbc_txtCookie.gridy = 0;
        panel.add( txtCookie, gbc_txtCookie );
        txtCookie.setColumns( 10 );

        JButton btnValider = new JButton( "Valider" );
        btnValider.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseClicked( MouseEvent e ) {
                if ( getID() != null && !txtCookie.getText().equals( "Saisir le cookie" ) ) {
                    try {
                        StringBuffer resultat = RequestTools.sendGet( "webvpn=" + txtCookie.getText(), getID() );
                        int rowCount = getTableModele().getRowCount();
                        for ( int i = rowCount - 1; i >= 0; i-- ) {
                            getTableModele().removeRow( i );
                        }
                        float sommeNotes = 0;
                        float sommeCoefficients = 0;
                        for ( String tmp : NOM_UE ) {
                            float coefficient = ParsingTools.recupererCoefficient( resultat, tmp );
                            float note = ParsingTools.recupererNote( resultat, tmp );
                            if ( note != -1 && coefficient != -1 ) {
                                sommeCoefficients += coefficient;
                                sommeNotes += note * coefficient;
                            }
                            getTableModele()
                                    .addRow( new Object[] { tmp, coefficient,
                                            note } );
                        }
                        getTableModele()
                                .addRow( new Object[] { "Somme des coefficients", sommeCoefficients,
                                } );
                        getTableModele()
                                .addRow( new Object[] { "Moyenne actuelle", sommeNotes / sommeCoefficients,
                                } );
                    } catch ( Exception e1 ) {
                        e1.printStackTrace();
                    }
                }
            }
        } );
        GridBagConstraints gbc_btnValider = new GridBagConstraints();
        gbc_btnValider.fill = GridBagConstraints.BOTH;
        gbc_btnValider.insets = new Insets( 0, 0, 5, 0 );
        gbc_btnValider.gridx = 1;
        gbc_btnValider.gridy = 0;
        panel.add( btnValider, gbc_btnValider );

        tableModele = new DefaultTableModel();
        tableModele.addColumn( "Nom de l'UE" );
        tableModele.addColumn( "Coefficient de l'UE" );
        tableModele.addColumn( "Note de l'UE" );

        @SuppressWarnings( "rawtypes" )
        JList list = new JList();
        TreeMap<String, Integer> map = ParsingTools.creerMap();
        list.setListData( map.keySet().toArray() );
        list.setValueIsAdjusting( true );

        list.addListSelectionListener( new ListSelectionListener() {
            @Override
            public void valueChanged( ListSelectionEvent arg0 ) {
                if ( !arg0.getValueIsAdjusting() ) {
                    setNom( list.getSelectedValue().toString() );
                    setID( map.get( list.getSelectedValue().toString() ).toString() );
                }
            }
        } );

        Label label = new Label( " Liste des étudiants :" );
        GridBagConstraints gbc_label = new GridBagConstraints();
        gbc_label.anchor = GridBagConstraints.WEST;
        gbc_label.insets = new Insets( 0, 0, 5, 5 );
        gbc_label.gridx = 0;
        gbc_label.gridy = 1;
        panel.add( label, gbc_label );

        Label label_1 = new Label( " Informations sur l'étudiant :" );
        GridBagConstraints gbc_label_1 = new GridBagConstraints();
        gbc_label_1.anchor = GridBagConstraints.WEST;
        gbc_label_1.insets = new Insets( 0, 0, 5, 0 );
        gbc_label_1.gridx = 1;
        gbc_label_1.gridy = 1;
        panel.add( label_1, gbc_label_1 );

        JScrollPane scrollPane_1 = new JScrollPane();
        GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
        gbc_scrollPane_1.insets = new Insets( 0, 0, 0, 5 );
        gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
        gbc_scrollPane_1.gridx = 0;
        gbc_scrollPane_1.gridy = 2;
        panel.add( scrollPane_1, gbc_scrollPane_1 );
        scrollPane_1.add( list );

        table = new JTable( tableModele );
        table.setToolTipText( "Tableau" );
        table.setBackground( Color.LIGHT_GRAY );
        table.setColumnSelectionAllowed( true );
        table.setFillsViewportHeight( true );

        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 1;
        gbc_scrollPane.gridy = 2;
        panel.add( scrollPane, gbc_scrollPane );
        scrollPane.add( table );
        scrollPane.setViewportView( table );
        scrollPane_1.setViewportView( list );
        pack();

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar( menuBar );

        JMenu mnAutres = new JMenu( "Autres" );
        menuBar.add( mnAutres );

        JMenuItem mntmRaliserLeClassement = new JMenuItem( "Réaliser le classement" );
        mntmRaliserLeClassement.addMouseListener( new MouseAdapter() {
            @Override
            public void mousePressed( MouseEvent e ) {
            }
        } );
        mnAutres.add( mntmRaliserLeClassement );
        validate();
    }

    public JTable getTable() {
        return table;
    }

    public void setTable( JTable table ) {
        this.table = table;
    }

    public JTextField getTxtCookie() {
        return txtCookie;
    }

    public void setTxtCookie( JTextField txtCookie ) {
        this.txtCookie = txtCookie;
    }

    public DefaultTableModel getTableModele() {
        return tableModele;
    }

    public void setTableModele( DefaultTableModel tableModele ) {
        this.tableModele = tableModele;
    }

    public String getID() {
        return ID;
    }

    public void setID( String iD ) {
        ID = iD;
    }

    public String getNom() {
        return nom;
    }

    public void setNom( String nom ) {
        this.nom = nom;
    }

}
