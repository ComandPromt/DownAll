package utils;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import principal.Descarga;

public class Metodos {

	public static String limpiarCadena(String urlObtenida) {

		if (urlObtenida.indexOf("\">") > 0 && urlObtenida.indexOf("</a>") > 0) {
			urlObtenida = urlObtenida.substring(urlObtenida.indexOf("\">") + 2, urlObtenida.indexOf("</a>"));
		}

		urlObtenida = urlObtenida.replace("</path>", "");
		urlObtenida = urlObtenida.replace("</svg>", "");
		urlObtenida = urlObtenida.replace("<span", "");
		urlObtenida = urlObtenida.replace("</span>", "");
		urlObtenida = urlObtenida.replace("<img>", "");
		urlObtenida = urlObtenida.replace("</img>", "");
		urlObtenida = urlObtenida.replace("itemscope", "");
		urlObtenida = urlObtenida.replace("</button>", "");
		urlObtenida = urlObtenida.replace("&nbsp;", "");
		urlObtenida = urlObtenida.replace("data-v-4992eadc", "");
		urlObtenida = urlObtenida.replace("data-v-4992eadcBlog", "");

		urlObtenida = urlObtenida.replaceAll("class=(.*)\"", "");
		urlObtenida = urlObtenida.replaceAll("onmouseover=(.*)\"", "");
		urlObtenida = urlObtenida.replaceAll("onmouseout=(.*)\"", "");
		urlObtenida = urlObtenida.replaceAll("height=(.*)\"", "");
		urlObtenida = urlObtenida.replaceAll("rel=(.*)\"", "");
		urlObtenida = urlObtenida.replaceAll("width=(.*)\"", "");
		urlObtenida = urlObtenida.replaceAll("itemprop=(.*)\"", "");
		urlObtenida = urlObtenida.replaceAll("itemtype=(.*)\"", "");
		urlObtenida = urlObtenida.replaceAll("itemprop=(.*)\"", "");
		urlObtenida = urlObtenida.replaceAll("<figure>(.*)</figure>", "");
		urlObtenida = urlObtenida.replaceAll("target=(.*)\"", "");
		urlObtenida = urlObtenida.replaceAll("style=(.*)\"", "");
		urlObtenida = urlObtenida.replaceAll("<p(.*)</p>", "");
		urlObtenida = urlObtenida.replaceAll("<svg(.*)>", "");
		urlObtenida = urlObtenida.replaceAll("<path(.*)>", "");

		urlObtenida = urlObtenida.replace("a href=\"", "");
		urlObtenida = urlObtenida.replace("\"  >", "");
		urlObtenida = urlObtenida.replace("<i ></i>", "");
		urlObtenida = urlObtenida.replace("<img >", "");
		urlObtenida = urlObtenida.replace(">", "");
		urlObtenida = urlObtenida.replace("<img", "");
		urlObtenida = urlObtenida.replace("data-v-23efff06", "");
		urlObtenida = urlObtenida.replace("<button", "");
		urlObtenida = urlObtenida.replace("»", "");
		urlObtenida = urlObtenida.replace("…", "");
		urlObtenida = urlObtenida.replace("???? ??????	", "");
		urlObtenida = urlObtenida.replace("src=//", "");
		urlObtenida = urlObtenida.replace("\"", "");
		urlObtenida = urlObtenida.replace("src=", "");
		urlObtenida = urlObtenida.replace("'", "");

		return urlObtenida;

	}

	public static String eliminarEspacios(String cadena) {

		cadena = cadena.trim();

		cadena = cadena.replace("  ", " ");

		cadena = cadena.trim();

		return cadena;
	}

	public static LinkedList<String> extraerEnlaces(String cadena, String url, String filtro) {

		LinkedList<String> urls = new LinkedList<String>();

		LinkedList<String> temporal = new LinkedList<String>();

		String residuo = "";

		int puntero = cadena.indexOf("http");

		int capacidad = 0;

		String cadenaEspacio = "";

		while (cadena.indexOf(" ") > 0) {

			if (puntero >= 0 && cadena.indexOf(" ") >= 0) {

				residuo = cadena.substring(0, cadena.indexOf(" "));

				if (residuo.contains("http") && residuo.contains(filtro)) {

					urls.add(cadena.substring(0, cadena.indexOf(" ")));

					if (capacidad > 0) {

						for (int i = 0; i < capacidad; i++) {
							cadenaEspacio += temporal.get(i) + " ";
						}

						if (!cadenaEspacio.equals(url + "#")) {

							urls.add(cadenaEspacio);
						}

						capacidad = 0;

						temporal.clear();

						cadenaEspacio = "";

					}

				}

				else {

					if (!residuo.contains(filtro)) {

						capacidad++;

						temporal.add(residuo);

					}

				}
			}

			cadena = cadena.substring(cadena.indexOf(" ") + 1, cadena.length());

		}

		if (!cadena.isEmpty() && cadena.contains(filtro) && !cadena.equals(url + "#") && cadena.indexOf("http") >= 0) {

			if (!urls.contains(cadena) && !cadena.contains("webcrawler")) {
				urls.add(cadena);
			}

		}

		return urls;
	}

	public static void copiarAlPortapapeles(String texto) {

		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();

		StringSelection testData;

		testData = new StringSelection(texto);

		c.setContents(testData, testData);
	}

	public static LinkedList<String> obtenerEnlaces(String url, int tomarUrl, String claseTabla, String claseTd,
			String filtro) {

		LinkedList<String> enlaces = new LinkedList<String>();

		try {

			Document doc = Jsoup.connect(url).get();

			Elements elements = null;

			String location = null;

			switch (tomarUrl) {

			case 1:
				elements = doc.select("a[href]");

				break;

			case 2:
				elements = doc.select("." + claseTabla + " tr:has(td." + claseTd + ") + tr");
				break;

			case 3:
				elements = doc.select("img[src]");
				break;

			case 4:
				elements = doc.select("." + claseTabla + " a[href]");
				break;

			case 5:
				elements = doc.select("source[src]");
				break;

			}

			for (Element element : elements) {

				switch (tomarUrl) {

				case 1:

				case 4:
					location = element.absUrl("href");
					break;

				case 2:
					location = element.previousElementSibling().select("td." + claseTd).text();
					break;

				case 3:

				case 5:
					location = element.absUrl("src");
					break;

				}

				location = eliminarEspacios(location);

				location = limpiarCadena(location);

				LinkedList<String> listaEnlaces = new <String>LinkedList();

				listaEnlaces = extraerEnlaces(location, url, filtro);

				for (int i = 0; i < listaEnlaces.size(); i++) {

					if (!listaEnlaces.get(i).equals(url + "/#") && !enlaces.contains(listaEnlaces.get(i))) {
						enlaces.add(listaEnlaces.get(i));
					}

				}

			}

		}

		catch (Exception e) {
			//
		}

		return enlaces;

	}

	public static boolean pingURL(String url) {

		int timeout = 400000;

		url = url.replaceFirst("^https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.

		try {

			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);
			connection.setRequestMethod("HEAD");

			int responseCode = connection.getResponseCode();

			if (responseCode == 404) {
				return false;
			}

			else {
				return (200 <= responseCode && responseCode <= 399);
			}

		} catch (IOException exception) {
			return false;
		}
	}

	public static void abrirCarpeta(String ruta) throws IOException {

		if (ruta != null && !ruta.equals("") && !ruta.isEmpty()) {

			try {

				if (Descarga.getOs().contentEquals("Linux")) {
					Runtime.getRuntime().exec("xdg-open " + ruta);
				}

				else {
					Runtime.getRuntime().exec("cmd /c explorer " + "\"" + ruta + "\"");
				}
			} catch (IOException e) {
				mensaje("Ruta inválida", 1);
			}
		}

	}

	public static String saberSeparador(String os) {
		if (os.equals("Linux")) {
			return "/";
		} else {
			return "\\";
		}
	}

	public static void mensaje(String mensaje, int titulo) {

		String tituloSuperior = "", sonido = "";

		int tipo = 0;

		switch (titulo) {

		case 1:
			tipo = JOptionPane.ERROR_MESSAGE;
			tituloSuperior = "Error";
			sonido = "duck-quack.wav";
			break;

		case 2:
			tipo = JOptionPane.INFORMATION_MESSAGE;
			tituloSuperior = "Informacion";
			sonido = "gong.wav";
			break;

		case 3:
			tipo = JOptionPane.WARNING_MESSAGE;
			tituloSuperior = "Advertencia";
			sonido = "advertencia.wav";
			break;

		default:
			break;

		}

		JLabel alerta = new JLabel(mensaje);

		alerta.setFont(new Font("Arial", Font.BOLD, 18));

		JOptionPane.showMessageDialog(null, alerta, tituloSuperior, tipo);

	}

	public void guardarDatos(Boolean mensaje) throws IOException {

		FileWriter flS = new FileWriter("Config/Config2.txt");

		BufferedWriter fS = new BufferedWriter(flS);

		try {

			System.out.println(Descarga.datos.get(0) + " - " + Descarga.urls.get(0));
//
//			fS.write(jTextField1.getText().trim());
//			fS.newLine();
//			fS.write(textField.getText().trim());
//			fS.close();
//			flS.close();
//			dispose();

			// MenuPrincipal.setLecturaurl(leerFicheroArray("Config2.txt", 2));

			if (Boolean.TRUE.equals(mensaje)) {

				mensaje("Archivo guardado con exito!", 2);
			}

		} catch (Exception e) {
			if (Boolean.TRUE.equals(mensaje)) {
				mensaje("Error al crear el fichero de configuracion", 1);
			}
		} finally {
			fS.close();
			flS.close();
		}
	}
}
