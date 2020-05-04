package principal;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import utils.Metodos;

@SuppressWarnings("all")

public class Descarga extends javax.swing.JFrame implements ActionListener, ChangeListener {
	public static JCheckBox primerEnlace = new JCheckBox("1er Link");
	JCheckBox enlaces = new JCheckBox("Enlaces");
	private JTextField target;
	static JCheckBox chckbxRecorrerPagina = new JCheckBox("Recorrer pagina?");
	public static LinkedList<String> urls = new LinkedList<String>();
	static DefaultListModel<String> modelo = new DefaultListModel<>();
	public static LinkedList<String> datos = new LinkedList<String>();
	private JTextField principio;
	private JTextField hasta;
	private JTextField paso;
	public static JTextField txtHttp;
	static JList list = new JList();
	static String listaUrls = "";
	JCheckBox imagen = new JCheckBox("Im\u00E1genes");
	JCheckBox musicaVideo = new JCheckBox("Audio y v\u00EDdeo");
	static String os = System.getProperty("os.name");
	JCheckBox web = new JCheckBox("Web");

	public static String getOs() {
		return os;
	}

	static String separador = Metodos.saberSeparador(os);

	public static void setTxtHttp(JTextField txtHttp) {
		Descarga.txtHttp = txtHttp;
	}

	public Descarga() {

		setTitle("DownAll");
		initComponents();
		this.setVisible(true);
	}

	public static LinkedList<String> getUrls() {
		return urls;
	}

	public static void setUrls(LinkedList<String> urls) {
		Descarga.urls = urls;
	}

	public static LinkedList<String> getDatos() {
		return datos;
	}

	public static void setDatos(LinkedList<String> datos) {
		Descarga.datos = datos;
	}

	protected void obtenerEnlaces() {

		urls.clear();

		modelo.clear();

		listaUrls = "";

		String urlTarget = Metodos.eliminarEspacios(target.getText());

		if (web.isSelected() && !urlTarget.contains("webcrawler.com")) {

			urlTarget = urlTarget.replace(" ", "+");

			if (musicaVideo.isSelected()) {

				urlTarget = "https://www.webcrawler.com/serp?q=" + urlTarget + "&sc=viZTfReTAd6Q20";

			}

			if (imagen.isSelected()) {
				urlTarget = "https://www.webcrawler.com/serp?q=" + urlTarget + "&sc=wDOnxSM80chv20";

			}

			if (enlaces.isSelected()) {
				urlTarget = "https://www.webcrawler.com/serp?qc=web&q=" + urlTarget + "&sc=coQXJrwSXKCW20";

			}

		}

		String filtroUrl = Metodos.eliminarEspacios(txtHttp.getText());

		if (urlTarget.isEmpty()) {
			Metodos.mensaje("Por favor, introduzca una URL", 3);
		}

		if (filtroUrl.isEmpty()) {
			txtHttp.setText("http");
			obtenerEnlaces();
		}

		if (!urlTarget.isEmpty() && !filtroUrl.isEmpty()) {

			int tipo = 0;

			String empezar = principio.getText();
			String contar = hasta.getText();
			String indice = paso.getText();

			if (enlaces.isSelected()) {
				tipo = 1;

			}

			if (musicaVideo.isSelected()) {

				if (web.isSelected()) {
					tipo = 1;
				}

				else {
					tipo = 5;
				}

			}

			if (imagen.isSelected()) {
				tipo = 3;

			}

			descargar(urlTarget, filtroUrl, tipo, empezar, contar, indice, true);

		}
	}

	protected static void descargar(String urlTarget, String filtroUrl, int tipo, String empezar, String hasta,
			String paso, boolean filtro) {

		if (tipo > 0) {

			if (!chckbxRecorrerPagina.isSelected() && filtro) {
				System.out.println("analizo sin dar vueltas: " + urlTarget);
				urls = Metodos.obtenerEnlaces(urlTarget, tipo, "", "", filtroUrl);

				for (int i = 0; i < urls.size(); i++) {

					if (!urls.get(i).contains("webcrawler")) {
						modelo.addElement(urls.get(i));

						listaUrls += urls.get(i) + "\n";
					}

				}

			}

			else {

				try {

					String principio, busqueda = "";

					int iniciar = Integer.parseInt(Metodos.eliminarEspacios(empezar));

					int contar = Integer.parseInt(Metodos.eliminarEspacios(hasta));

					int puntero = Integer.parseInt(Metodos.eliminarEspacios(paso));

					urlTarget = Metodos.eliminarEspacios(urlTarget);

					if (iniciar <= contar && puntero > 0) {

						principio = urlTarget.substring(0, urlTarget.indexOf("&sc"));

						busqueda = urlTarget.substring(urlTarget.indexOf("&sc=") + 4, urlTarget.length());

						int limite = 1;

						for (int i = iniciar; i <= contar; i += puntero) {

							urlTarget = principio + "+" + i + "&sc=" + busqueda;

							urls = Metodos.obtenerEnlaces(urlTarget, tipo, "", "", filtroUrl);

							if (!primerEnlace.isSelected()) {
								limite = urls.size();
							}

							for (int x = 0; x < limite; x++) {

								modelo.addElement(urls.get(x));

								listaUrls += urls.get(x) + "\n";
							}

							urls.clear();

						}

					}

					else {
						Metodos.mensaje("Por favor, introduce correctamente los datos", 3);
					}

				}

				catch (Exception e) {
					Metodos.mensaje("Por favor, introduce correctamente los datos", 3);

				}
			}

			list.setModel(modelo);
		}
	}

	private void initComponents() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		setResizable(false);

		target = new JTextField();
		target.setText("http://");
		target.setHorizontalAlignment(SwingConstants.CENTER);
		target.setFont(new Font("Tahoma", Font.PLAIN, 16));
		target.setColumns(10);

		JLabel lblNewLabel = new JLabel("URL (target)");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setIcon(new ImageIcon(Descarga.class.getResource("/imagenes/target.png")));
		chckbxRecorrerPagina.setHorizontalAlignment(SwingConstants.CENTER);

		chckbxRecorrerPagina.setFont(new Font("Tahoma", Font.PLAIN, 16));

		principio = new JTextField();
		principio.setHorizontalAlignment(SwingConstants.CENTER);
		principio.setFont(new Font("Tahoma", Font.PLAIN, 16));
		principio.setColumns(10);

		hasta = new JTextField();
		hasta.setHorizontalAlignment(SwingConstants.CENTER);
		hasta.setFont(new Font("Tahoma", Font.PLAIN, 16));
		hasta.setColumns(10);

		paso = new JTextField();
		paso.setHorizontalAlignment(SwingConstants.CENTER);
		paso.setFont(new Font("Tahoma", Font.PLAIN, 16));
		paso.setColumns(10);

		JLabel label = new JLabel("Paso");
		label.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JLabel label_2 = new JLabel("Hasta");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JLabel lblDe = new JLabel("  De");
		lblDe.setFont(new Font("Tahoma", Font.PLAIN, 16));

		txtHttp = new JTextField();

		txtHttp.addKeyListener(new KeyAdapter() {

			@Override

			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					obtenerEnlaces();
				}

			}

		});

		txtHttp.setText("http");
		txtHttp.setHorizontalAlignment(SwingConstants.CENTER);
		txtHttp.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtHttp.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("URLs que contienen");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JButton btnNewButton_1_1 = new JButton("Obtener URLs");

		btnNewButton_1_1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				obtenerEnlaces();

			}

		});

		btnNewButton_1_1.setIcon(new ImageIcon(Descarga.class.getResource("/imagenes/download.png")));
		btnNewButton_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JButton btnNewButton_1_2 = new JButton("Abrir URL");

		btnNewButton_1_2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {

					String url = Metodos.eliminarEspacios(target.getText());

					if (Metodos.pingURL(url)) {
						Metodos.abrirCarpeta(url);
					}

				}

				catch (Exception e1) {
					//
				}
			}

		});

		btnNewButton_1_2.setIcon(new ImageIcon(Descarga.class.getResource("/imagenes/remote.png")));
		btnNewButton_1_2.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JScrollPane scrollPane = new JScrollPane((Component) null);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Metodos.copiarAlPortapapeles(listaUrls);
			}
		});
		btnNewButton.setIcon(new ImageIcon(Descarga.class.getResource("/imagenes/copy.png")));

		enlaces.setSelected(true);
		enlaces.setFont(new Font("Tahoma", Font.PLAIN, 16));

		imagen.setFont(new Font("Tahoma", Font.PLAIN, 16));

		musicaVideo.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JButton btnNewButton_3 = new JButton("");

		btnNewButton_3.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (list.getModel().getSize() > 0 && JOptionPane.showConfirmDialog(null, "Quieres borrar la lista",
						"Borrar enlaces", JOptionPane.YES_NO_OPTION) == 0) {

					modelo.removeAllElements();

					list.setModel(modelo);

				}

			}

		});

		btnNewButton_3.setIcon(new ImageIcon(Descarga.class.getResource("/imagenes/clean.png")));

		web.setFont(new Font("Tahoma", Font.PLAIN, 16));

		primerEnlace.setFont(new Font("Tahoma", Font.PLAIN, 16));
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.TRAILING).addGroup(layout.createSequentialGroup()
				.addGap(25)
				.addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(Alignment.LEADING)
										.addGroup(layout.createSequentialGroup().addComponent(enlaces).addGap(72)
												.addComponent(musicaVideo))
										.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
												.addComponent(chckbxRecorrerPagina, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addGroup(layout.createSequentialGroup()
														.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
																.addComponent(lblDe, GroupLayout.DEFAULT_SIZE,
																		GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addComponent(principio, GroupLayout.PREFERRED_SIZE, 36,
																		GroupLayout.PREFERRED_SIZE))
														.addGap(46)
														.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
																.addComponent(label_2, GroupLayout.DEFAULT_SIZE,
																		GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addComponent(hasta, GroupLayout.DEFAULT_SIZE, 55,
																		Short.MAX_VALUE))
														.addGap(35)
														.addGroup(layout.createParallelGroup(Alignment.LEADING)
																.addComponent(label, GroupLayout.PREFERRED_SIZE, 36,
																		GroupLayout.PREFERRED_SIZE)
																.addComponent(paso, GroupLayout.PREFERRED_SIZE, 36,
																		GroupLayout.PREFERRED_SIZE)))))
								.addGroup(layout.createParallelGroup(Alignment.LEADING)
										.addGroup(layout.createSequentialGroup().addGap(27).addComponent(lblNewLabel)
												.addGap(70))
										.addGroup(layout.createSequentialGroup().addGap(73).addComponent(imagen))
										.addGroup(layout.createSequentialGroup()
												.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblNewLabel_2,
														GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE))))
								.addComponent(btnNewButton_1_1, GroupLayout.PREFERRED_SIZE, 468,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(Alignment.LEADING, false).addComponent(target)
								.addComponent(txtHttp)
								.addComponent(btnNewButton_1_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addGroup(layout.createSequentialGroup()
										.addComponent(web, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(primerEnlace, GroupLayout.PREFERRED_SIZE, 95,
												GroupLayout.PREFERRED_SIZE))))
						.addGroup(layout.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 528, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(btnNewButton_3, GroupLayout.PREFERRED_SIZE, 55,
										GroupLayout.PREFERRED_SIZE)
								.addGap(18).addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 53,
										GroupLayout.PREFERRED_SIZE)))
				.addGap(74)));
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup()
				.addGap(18)
				.addGroup(layout.createParallelGroup(Alignment.TRAILING).addGroup(layout
						.createParallelGroup(Alignment.LEADING)
						.addComponent(label, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(Alignment.LEADING)
										.addComponent(lblDe, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
										.addGroup(layout.createSequentialGroup()
												.addPreferredGap(ComponentPlacement.RELATED).addComponent(label_2,
														GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(layout.createParallelGroup(Alignment.BASELINE)
										.addComponent(principio, GroupLayout.PREFERRED_SIZE, 26,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(paso, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
										.addComponent(hasta, GroupLayout.PREFERRED_SIZE, 26,
												GroupLayout.PREFERRED_SIZE))))
						.addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(chckbxRecorrerPagina, GroupLayout.PREFERRED_SIZE, 29,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel)
								.addComponent(target, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
								.addGap(29)
								.addGroup(layout.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblNewLabel_2, GroupLayout.PREFERRED_SIZE, 19,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(txtHttp, GroupLayout.PREFERRED_SIZE, 26,
												GroupLayout.PREFERRED_SIZE))))
				.addGap(30)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(enlaces).addComponent(musicaVideo)
						.addComponent(imagen)
						.addComponent(web, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
						.addComponent(primerEnlace, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
				.addGap(30)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton_1_1, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_1_2, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 227, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_3, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 41,
								GroupLayout.PREFERRED_SIZE))
				.addContainerGap(34, Short.MAX_VALUE)));
		list.setFont(new Font("Tahoma", Font.PLAIN, 16));

		scrollPane.setViewportView(list);
		getContentPane().setLayout(layout);
		setSize(new Dimension(793, 635));
		setLocationRelativeTo(null);
	}

	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub

	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
