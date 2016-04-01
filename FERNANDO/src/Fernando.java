import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;


public class Fernando {

	public static void main(String[] args) {
		BufferedReader br=null;
		PrintWriter pw=null;
		try {
			br = new BufferedReader(new FileReader(args[0]));

			pw=new PrintWriter("out_"+args[0]);


			//cabeceras
			String line = br.readLine();	
			String[] part=line.split(";");

			String[] conceptos = new String[31];
			for (int i =0;i<31;i++) {
				conceptos[i]=part[i+10];
			}

			//lineas con datos
			line=br.readLine();
			while (line!=null) {
				part=line.split(";");
				//1-10 es el siniestro
				//11-42 son los puntos
				String siniestro=toCSV(part,0,9);

				for (int i=0;i<31;i++) {
					String out="";
					out+=siniestro+";";
					out+=conceptos[i]+";";
					out+=part[10+i];
					pw.write(out+"\n");
				}


				//lee la siguiente linea
				line=br.readLine();
			}



		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				pw.flush();
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public static String toCSV(String[] a) {
		String s = "";
		for (int i=0;i<a.length-1;i++) {
			s+=a[i]+";";
		}
		s+=a[a.length-1];

		return s;
	}

	public static String toCSV(String[] a, int ini, int fin) {
		String s = "";
		for (int i=ini;i<fin;i++) {
			s+=a[i]+";";
		}
		s+=a[fin];

		return s;
	}

}
