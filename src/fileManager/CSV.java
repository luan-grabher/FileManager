package fileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CSV {

    /**
     * Transforma texto de um arquivo CSV em um Mapa. Caso a primeira linha
     * comece com #, a primeira linha sera usada como header, caso o contrário,
     * as colunas serão nomeadas com o número da coluna.
     * <p>
     * Quebra colunas por ";" Quebra linhas por "\r\n"
     *
     * @param file Arquivo csv
     * @return Lista de linhas do csv
     */
    public static List<Map<String, String>> getMap(File file) {
        return getMap(file, ";");
    }

    /**
     * Transforma texto de um arquivo CSV em um Mapa. Caso a primeira linha
     * comece com #, a primeira linha sera usada como header, caso o contrário,
     * as colunas serão nomeadas com o número da coluna.
     * <p>
     * Quebra colunas por ";" Quebra linhas por "\r\n"
     *
     * @param colBreaker texto para quebrar as colunas, por padrao use ";"
     * @param file Arquivo csv
     * @return Lista de linhas do csv
     */
    public static List<Map<String, String>> getMap(File file, String colBreaker) {
        List<Map<String, String>> rows = new ArrayList<>();

        if (file.exists()) {
            String text = FileManager.getText(file);
            rows.addAll(getMapFromText(text, colBreaker));
        }

        return rows;
    }

    /**
     * Transforma texto em um Mapa. Caso a primeira linha
     * comece com #, a primeira linha sera usada como header, caso o contrário,
     * as colunas serão nomeadas com o número da coluna.
     * <p>
     * Quebra colunas por ";" Quebra linhas por "\r\n"
     *
     * @param colBreaker texto para quebrar as colunas, por padrao use ";"
     * @param text Arquivo texto
     * @return Lista de linhas do csv
     */
    public static List<Map<String, String>> getMapFromText(String text, String colBreaker) {
        List<Map<String, String>> rows = new ArrayList<>();
        String[] lines = text.split("\r\n");

        //Cria o header
        Map<Integer, String> headers = new LinkedHashMap<>();
        String[] firstRowCols = lines[0].replaceFirst("#", "").split(colBreaker);

        //Se tiver # na primeira linha, irá considerar como as colunas
        if (lines[0].startsWith("#")) {
            for (int i = 0; i < firstRowCols.length; i++) {
                String col = firstRowCols[i];
                //coloca no mapa
                headers.put(i, col);
            }
        }//Se não irá contar quantas colunas tem na primeira linha e irá definir como as colunas
        else {
            for (Integer i = 0; i < firstRowCols.length; i++) {
                headers.put(i, i.toString());
            }
        }

        //Percorre linhas
        for (String line : lines) {
            if (!line.startsWith("#")) {
                //Pega colunas da linha
                String[] cols = line.split(colBreaker,-1);

                //tryCatch para evitar parar tudo caso alguma linha tenha mais colunas que a primeira
                try {
                    Map<String, String> map = new LinkedHashMap<>();

                    //Para cada header coloca no mapa
                    headers.forEach((i, name) -> {
                        Boolean issetCol = cols.length > i;
                        String value = issetCol ? cols[i] : "";
                        map.put(name, value);
                    });

                    //coloca mapa na lista
                    rows.add(map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return rows;
    }
}
