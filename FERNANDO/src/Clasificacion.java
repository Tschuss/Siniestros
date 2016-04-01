import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Clasificacion {


	//public static void main(String[] args) throws IOException{
	/* no debeis cambiar la signatura de los metodos,
	 * MAIN es un metodo "del sistema" y no debe lanzar execpciones
	 * En el ECLIPE puedes pulsar en los iconos de aviso que salen junto al numero de linea
	 * y el propio editor te ayuda a incorporar el control de Excepcoines
	 */

	public static void main(String[] args) {

		//Array con los 60 aeródromos que difunden METAR
		String[] aeropuertos={"LEMD","LECV","LETO","LEGT","LEVS","LECO","LEST","LEVX","LEAS","LEVJ","LELN","LEBG","LEVD","LESA","LEBB","LEVT","LESO","LEPP","LELO","LEBR","LEHC","LEZG","LETL","LEDA","LESU","LEGE","LELL","LEBL","LERS","LECH","LEBT","LEVC","LEAB","LEIB","LEPA","LEMH","LEAL","LELC","LERI","LEAO","LEBA","LEGA","LEGR","LEAM","LEMG","GEML","LEBZ","LEZL","LEMO","LEEC","LEJR","LERT","GCRR","GCFV","GCLP","GCXO","GCTS","GCGM","GCHI","GCLA"};

		//Para lectura
		//FileReader lectura=new FileReader(".\\fuentes\\METAR_201004.txt");
		//BufferedReader br=new BufferedReader(lectura);
		/* cuando necesites un objeto para construir otro, intentad no crear variables intermedias
		 * LECTURA es una variable que no vais a usar en el programa, solo usareis BR
		 * anidad los "news" uno dentro del otro
		 */
		//BufferedReader br = new BufferedReader(new FileReader(".\\fuentes\\METAR_201004.txt"));


		/* No paseis como constantes los nombres de fichero, usad para eso los argumentos de la "linea de comando"
		 * java Clasificacion "./metar_XXXX.txt"
		 * los argumentos, entrecomillados, vienen en el parametro ARGS, el primero es el cero
		 * 
		 * en ECLIPSE, desde el menu RUN, eliges RUN CONFIGURATIONS, pestaña ARGUMENTS, PROGRAM ARGUMENTS y allí escribes el nombre del fichero
		 * 
		 * NOTA: si estais en UNIX/LINUX usad las barras de dividir, no la barra invertida de windows.
		 * 
		 * con los dos cambios aplicados, que así:
		 * 
		 * NOTA: java te obliga a meter un mini try/catch aqui... Eclipse lo genera automaticamente
		 */

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(args[0]));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		String linea="";

		//Para escritura


		/* esto lo haceis para generar tantos ficheros de salida como aeropuertos, no?
		 * pero no lo teneis terminado, por lo que veo haceis muchos FILEWRITER pero teneis un unico PrintWriter :-(
		 * recordad que no hace falta ese array intermedio de FileWriter, podeis hacerlo de PrintWriter directamente anidando los dos NEW
		 */

		String nombre;
		// FileWriter[]escritura=new FileWriter[aeropuertos.length];
		PrintWriter[] escritura=new PrintWriter[aeropuertos.length];

		// PrintWriter pw;
		for(int i=0; i<aeropuertos.length;i++){
			nombre="METAR_"+aeropuertos[i]+".txt";
			/* un PrintWriter se puede generar direstamente desde un FILE que es un objeto más pequeño
			 * anidando los NEW y cambiando FileWriter por File queda así:
			 * 
			 * NOTA: java te obliga a meter un mimi try/catch aqui, pero Eclise te lo escribe solo (como os contaba antes)
			 */
			try {
				escritura[i] =new PrintWriter(new FileWriter(nombre,true));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		// Proceso
		/* este FOR no tiene mucho sentido, porque se os terminará el BR.READLINE antes de que le deis la segunda vuelta al FOR.
		 * entiendo que teneis un METAR con todos los aeropuestos mezclados?
		 * tendreis que ver un modo de sacar de cada linea del metar a qué aeropuerto le toca para elegir en que PRINTWRITER hacer la escritura 
		 * y así ir cribando cada linea del METAR y escribiendola en un archido de escritura distinto.
		 * Un modo facil es que si en el METAR pone "LEMD","LECV","LETO",etc. useis eso como parte del nomrbre del fichero destino.
		 * Tambien teneis otros objectos, como los COLLECTION que os pueden ayudar: MAP o HASHMAP pueden ser buena opcion, usando como KEY el codigo del aeropuesto y como VALUE el PrinrWriter del archivo que toque
		 * 
		 */
		for(int i=0; i<aeropuertos.length;i++){            
			/* el TRY debe estar dentro del blule
			 * así si un fichero está mal y los otros 30 están bien... se generarán 29 ficheros y un error.
			 */
			try {
				linea=br.readLine();
				//este trabajo ya lo llevamos hecho de antes
				//pw=new PrintWriter(escritura[i]);
				while(linea!=null){
					//System.out.println(linea);
					if(linea.contains(aeropuertos[i]))
					{                
						System.out.println(linea);
						/* ahora puedes usar directamente ESCRITURA que ya es un array de PrintWriters
						 * 
						 */
						//pw..println(linea);
						escritura[i].println(linea);
					}
					linea=br.readLine();
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				/* FINALLY es una seccion del TRY/CATCH que se ejecuta al final de ambos...
				 * si ha funcionado bien se ejecuta despues de la ultima linea del TRY
				 * si ha salido una excepcion se ejecuta despues de la ultima linea del CATCH correspondiente
				 * aqui es donde se cierran los reader y los writers, de modo que si sales con una excepcion, tambien se cierre todo, 
				 * y no pierdas el trodo de ejecución que ha ido bien :-)
				 */

				if (escritura[i]!=null) {
					//lectura ya no existe
					//lectura=new FileReader(".\\fuentes\\METAR_201004.txt");
					//br=new BufferedReader(lectura);

					/* antes de hacer CLOSE teneis que hacer FLUSH y antes de eso ver si el objeto se ha creado bien 
					 * (mirando que no sea null) 
					 */		
					escritura[i].flush();
					escritura[i].close();
				}
				if (br != null) {
					/*tambien cerramos el reader, aunque aqui no se hace FLUSH porque no has escrito, solo has leido
					 * un reader consume del Sistema Operativo un acceso al disco, si no lo cierras, puedes tener problemas y quedarte sin recursos del SO
					 * 
					 * el control de errores de JAVA te obliga a meter un "mini" try/catch aqui... 
					 */
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}    
	}
}