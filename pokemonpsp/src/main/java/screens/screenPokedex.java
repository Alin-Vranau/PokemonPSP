package screens;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.JTextPane;

public class screenPokedex extends JFrame {
	private String[] nombresColumnas = { "Nº", "Nombre" };
	private static Object[][] listaPokemons = new Object[151][];
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		llenarListaPokemon();

		// JSONArray

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					screenPokedex frame = new screenPokedex();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void llenarListaPokemon() {
		try {
			// Realizar una solicitud GET a la PokeAPI para obtener información sobre los
			// Pokémon
			URL url = new URL("https://pokeapi.co/api/v2/pokemon/?limit=151");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// Analizar la respuesta JSON
			JSONObject jsonResponse = new JSONObject(response.toString());
			JSONArray results = jsonResponse.getJSONArray("results");

			for (int i = 0; i < 151; i++) {

				listaPokemons[i] = new Object[2];
				listaPokemons[i][0] = i + 1;
				listaPokemons[i][1] = results.getJSONObject(i).getString("name").toUpperCase();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame.
	 */
	public screenPokedex() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 662, 447);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel pnBanner = new JPanel();
		pnBanner.setBackground(new Color(87, 77, 79));
		pnBanner.setBounds(0, 0, 647, 65);
		contentPane.add(pnBanner);
		pnBanner.setLayout(null);

		JLabel lblNewLabel = new JLabel("POKEDEX");
		lblNewLabel.setForeground(new Color(255, 233, 153));
		lblNewLabel.setBackground(new Color(255, 217, 82));
		lblNewLabel.setBounds(221, 10, 173, 44);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 36));
		pnBanner.add(lblNewLabel);

		JPanel pnContent = new JPanel();
		pnContent.setBackground(new Color(255, 217, 82));
		pnContent.setBounds(0, 65, 494, 349);
		contentPane.add(pnContent);
		pnContent.setLayout(null);

		JTextArea textDescription = new JTextArea();
		textDescription.setEditable(false);
		textDescription.setBackground(new Color(255, 233, 153));
		textDescription.setBounds(69, 276, 323, 47);
		pnContent.add(textDescription);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 233, 153));
		panel.setBounds(273, 67, 200, 194);
		pnContent.add(panel);
		panel.setLayout(null);

		JLabel lblImagen = new JLabel("");
		lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
		lblImagen.setBounds(10, 11, 180, 174);
		panel.add(lblImagen);

		JLabel lblNomPokemon = new JLabel("BULBASAUR");
		lblNomPokemon.setForeground(new Color(87, 77, 79));
		lblNomPokemon.setHorizontalAlignment(SwingConstants.CENTER);
		lblNomPokemon.setFont(new Font("Tahoma", Font.BOLD, 28));
		lblNomPokemon.setBounds(212, 11, 260, 39);
		pnContent.add(lblNomPokemon);

		JLabel lblNumPokemon = new JLabel("Nº. 1");
		lblNumPokemon.setForeground(new Color(87, 77, 79));
		lblNumPokemon.setHorizontalAlignment(SwingConstants.LEFT);
		lblNumPokemon.setFont(new Font("Tahoma", Font.BOLD, 28));
		lblNumPokemon.setBounds(21, 11, 142, 38);
		pnContent.add(lblNumPokemon);

		JTextPane textHeight = new JTextPane();
		textHeight.setText("0.7m");
		textHeight.setEditable(false);
		textHeight.setForeground(new Color(87, 77, 79));
		textHeight.setFont(new Font("Tahoma", Font.BOLD, 14));
		textHeight.setBackground(new Color(255, 233, 153));
		textHeight.setBounds(110, 72, 131, 23);
		pnContent.add(textHeight);

		JTextPane textWeight = new JTextPane();
		textWeight.setText("6.9kg");
		textWeight.setEditable(false);
		textWeight.setForeground(new Color(87, 77, 79));
		textWeight.setFont(new Font("Tahoma", Font.BOLD, 14));
		textWeight.setBackground(new Color(255, 233, 153));
		textWeight.setBounds(109, 106, 132, 23);
		pnContent.add(textWeight);

		JTextPane textType1 = new JTextPane();
		textType1.setToolTipText("");
		textType1.setText("GRASS");
		textType1.setEditable(false);
		textType1.setForeground(new Color(87, 77, 79));
		textType1.setFont(new Font("Tahoma", Font.BOLD, 14));
		textType1.setBackground(new Color(0, 193, 93));
		textType1.setBounds(110, 176, 89, 23);
		pnContent.add(textType1);

		JTextPane textHabitat = new JTextPane();
		textHabitat.setText("GRASSLAND");
		textHabitat.setEditable(false);
		textHabitat.setForeground(new Color(87, 77, 79));
		textHabitat.setFont(new Font("Tahoma", Font.BOLD, 14));
		textHabitat.setBackground(new Color(255, 233, 153));
		textHabitat.setBounds(110, 140, 131, 23);
		pnContent.add(textHabitat);

		JTextPane textType2 = new JTextPane();
		textType2.setText("POISON");
		textType2.setEditable(false);
		textType2.setForeground(new Color(87, 77, 79));
		textType2.setFont(new Font("Tahoma", Font.BOLD, 14));
		textType2.setBackground(new Color(160, 64, 160));
		textType2.setBounds(110, 210, 89, 23);
		pnContent.add(textType2);

		JTextPane txtpnHeight = new JTextPane();
		txtpnHeight.setForeground(new Color(255, 233, 153));
		txtpnHeight.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtpnHeight.setBackground(new Color(87, 77, 79));
		txtpnHeight.setText("HEIGHT:");
		txtpnHeight.setBounds(19, 72, 81, 23);
		pnContent.add(txtpnHeight);

		JTextPane txtpnWeight = new JTextPane();
		txtpnWeight.setText("WEIGHT:");
		txtpnWeight.setForeground(new Color(255, 233, 153));
		txtpnWeight.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtpnWeight.setBackground(new Color(87, 77, 79));
		txtpnWeight.setBounds(18, 106, 81, 23);
		pnContent.add(txtpnWeight);

		JTextPane txtpnHabitat = new JTextPane();
		txtpnHabitat.setText("HABITAT:");
		txtpnHabitat.setForeground(new Color(255, 233, 153));
		txtpnHabitat.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtpnHabitat.setBackground(new Color(87, 77, 79));
		txtpnHabitat.setBounds(19, 144, 81, 23);
		pnContent.add(txtpnHabitat);

		JTextPane txtpnTypo = new JTextPane();
		txtpnTypo.setText("TYPO:");
		txtpnTypo.setForeground(new Color(255, 233, 153));
		txtpnTypo.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtpnTypo.setBackground(new Color(87, 77, 79));
		txtpnTypo.setBounds(19, 176, 81, 23);
		pnContent.add(txtpnTypo);

		JPanel pnTable = new JPanel();
		pnTable.setBackground(new Color(150, 144, 145));
		pnTable.setBounds(493, 65, 154, 345);
		contentPane.add(pnTable);
		pnTable.setLayout(null);

		// Crear la tabla
		JTable pokeTable = new JTable(new DefaultTableModel(listaPokemons, nombresColumnas)) {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if (isRowSelected(row) && isColumnSelected(column)) {
					((JComponent) c).setBorder(BorderFactory.createEmptyBorder());
				}
				return c;
			}

			boolean[] columnEditables = new boolean[] {
					false, false
			};

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};

		// JTable pokeTable = new JTable (listaPokemons, nombresColumnas);
		pokeTable.setFont(new Font("Tahoma", Font.BOLD, 12));
		pokeTable.setBackground(new Color(150, 144, 145));
		pokeTable.setSelectionBackground(new Color(87, 77, 79));
		pokeTable.getTableHeader().setBackground(new Color(87, 77, 79));
		pokeTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
		pokeTable.setShowGrid(false);
		pokeTable.setModel(new DefaultTableModel(
				listaPokemons,
				new String[] {
						"N\u00BA", "Nombre"
				}));
		pokeTable.getColumnModel().getColumn(0).setPreferredWidth(35);
		pokeTable.getColumnModel().getColumn(0).setMaxWidth(50);
		pokeTable.setRowSelectionInterval(0, 0);

		JPanel panelRellenoScroll = new JPanel();
		panelRellenoScroll.setBackground(new Color(87, 77, 79));

		JScrollPane scroll = new JScrollPane(pokeTable);
		scroll.setBounds(0, 0, 154, 345);
		scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
			@Override
			protected void configureScrollBarColors() {
				this.thumbColor = new Color(87, 77, 79);
				this.trackColor = new Color(150, 144, 145);
			}

			@Override
			protected JButton createDecreaseButton(int orientation) {
				JButton button = super.createDecreaseButton(orientation);
				button.setBackground(new Color(87, 77, 79));
				return button;
			}

			@Override
			protected JButton createIncreaseButton(int orientation) {
				JButton button = super.createIncreaseButton(orientation);
				button.setBackground(new Color(87, 77, 79));
				return button;
			}
		});

		scroll.setHorizontalScrollBarPolicy(
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(BorderFactory.createLineBorder(new Color(87, 77, 79), 2));
		scroll.getViewport().setBackground(new Color(87, 77, 79));
		scroll.setCorner(JScrollPane.UPPER_RIGHT_CORNER, panelRellenoScroll);
		pnTable.add(scroll);

		ImageIcon bulbasaur = null;
		try {
			bulbasaur = new ImageIcon(new URL(
					"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/showdown/1.gif"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lblImagen.setIcon(bulbasaur);
		textDescription.setText("Una rara semilla le fue plantada en el lomo al nacer.\r\n"
				+ "La planta brota y crece con este Pokémon.");

		pokeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					int selectedRow = pokeTable.getSelectedRow();
					lblNumPokemon.setText("Nº. " + listaPokemons[selectedRow][0]);
					lblNomPokemon.setText((String) listaPokemons[selectedRow][1]);

					try {

						URL urlPokemon = new URL("https://pokeapi.co/api/v2/pokemon/" + (selectedRow + 1));
						URL urlDescription = new URL("https://pokeapi.co/api/v2/pokemon-species/" + (selectedRow + 1));
						HttpURLConnection conPokemon = (HttpURLConnection) urlPokemon.openConnection();
						HttpURLConnection conSpecies = (HttpURLConnection) urlDescription.openConnection();
						conPokemon.setRequestMethod("GET");
						conSpecies.setRequestMethod("GET");

						String inputLine;

						BufferedReader in = new BufferedReader(
								new InputStreamReader(conPokemon.getInputStream(), StandardCharsets.UTF_8));
						StringBuilder responsePokemon = new StringBuilder();
						while ((inputLine = in.readLine()) != null) {
							responsePokemon.append(inputLine);
						}
						in.close();

						BufferedReader inSpecies = new BufferedReader(
								new InputStreamReader(conSpecies.getInputStream(), StandardCharsets.UTF_8));
						StringBuilder responseSpecies = new StringBuilder();
						while ((inputLine = inSpecies.readLine()) != null) {
							responseSpecies.append(inputLine);
						}
						inSpecies.close();

						// Imagen
						JSONObject jsonResponse = new JSONObject(responsePokemon.toString());
						String frontDefaultGif = jsonResponse.getJSONObject("sprites").getJSONObject("other")
								.getJSONObject("showdown").getString("front_default");
						String frontDefaultPng = jsonResponse.getJSONObject("sprites").getJSONObject("versions")
								.getJSONObject("generation-vi").getJSONObject("x-y").getString("front_default");
						URL frontDefaultPngUrl = new URL(frontDefaultPng);
						URL frontDefaultGifUrl = new URL(frontDefaultGif);

						ImageIcon imgPokemon = new ImageIcon(frontDefaultGifUrl);
						lblImagen.setIcon(imgPokemon);

						// Datos
						textHeight.setText((jsonResponse.getDouble("height") / 10) + "m");
						textWeight.setText((jsonResponse.getDouble("weight") / 10) + "kg");

						// tipos

						String tipo1 = jsonResponse.getJSONArray("types").getJSONObject(0).getJSONObject("type")
								.getString("name").toUpperCase();
						String tipo2 = null;
						try {
							tipo2 = jsonResponse.getJSONArray("types").getJSONObject(1).getJSONObject("type")
									.getString("name").toUpperCase();
						} catch (Exception e2) {
							tipo2 = "";
						}

						textType1.setText(tipo1);
						textType1.setBackground(colorTipo(tipo1));
						textType2.setText(tipo2);
						textType2.setBackground(colorTipo(tipo2));

						// Descripcion
						JSONObject jsonResponseSpecies = new JSONObject(responseSpecies.toString());
						String descrption;
						int posicion = 0;
						for (int i = 0; i < 40; i++) {
							String pais = jsonResponseSpecies.getJSONArray("flavor_text_entries").getJSONObject(i)
									.getJSONObject("language").getString("name");
							if (pais.equals("es")) {
								posicion = i;
								break;
							}
						}
						descrption = jsonResponseSpecies.getJSONArray("flavor_text_entries").getJSONObject(posicion)
								.getString("flavor_text");
						textDescription.setText(descrption);
						// Habitat
						textHabitat
								.setText(jsonResponseSpecies.getJSONObject("habitat").getString("name").toUpperCase());

					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}

	public Color colorTipo(String tipo) {
		Color color = new Color(255, 217, 82);
		switch (tipo) {
			case "WATER":
				color = new Color(104, 144, 240);
				break;
			case "FIGHTING":
				color = new Color(232, 48, 0);
				break;
			case "FLYING":
				color = new Color(152, 216, 216);
				break;
			case "POISON":
				color = new Color(160, 64, 160);
				break;
			case "GROUND":
				color = new Color(153, 77, 0);
				break;
			case "ROCK":
				color = new Color(184, 160, 56);
				break;
			case "BUG":
				color = new Color(120, 200, 80);
				break;
			case "GHOST":
				color = new Color(83, 33, 83);
				break;
			case "STEEL":
				color = new Color(102, 153, 174);
				break;
			case "FIRE":
				color = new Color(240, 128, 48);
				break;
			case "GRASS":
				color = new Color(0, 193, 93);
				break;
			case "ELECTRIC":
				color = new Color(248, 176, 16);
				break;
			case "PSYCHIC":
				color = new Color(248, 88, 136);
				break;
			case "ICE":
				color = new Color(178, 254, 254);
				break;
			case "DRAGON":
				color = new Color(70, 98, 163);
				break;
			case "DARK":
				color = new Color(35, 52, 59);
				break;
			case "FAIRY":
				color = new Color(113, 140, 206);
				break;
			case "UNKNOWN":
				color = new Color(113, 140, 206);
				break;
			case "SHADOW":
				color = new Color(113, 140, 206);
				break;
			case "NORMAL":
				color = new Color(168, 168, 120);
				break;
		}
		return color;
	}
}