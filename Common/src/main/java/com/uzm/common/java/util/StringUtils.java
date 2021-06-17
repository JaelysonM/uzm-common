package com.uzm.common.java.util;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe com utilitários relacionado a {@link String}.
 *
 * @author Maxter & JotaMPê
 */
public class StringUtils {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###");

    private static final int MAX_LENGTH = 4;

    private static final String[] suffix = new String[]{"", "k", "m", "b", "t"};

    public static final String[] NUMBER_FORMAT = {"K", "M", "B", "T", "Q", "QQ", "S", "SS", "OC", "N", "D", "UN", "DD", "TR", "QT", "QN", "SD", "SPD", "OD", "ND", "VG", "UVG"};

    protected static final String[] DAY_OF_WEEK = new String[]{"domingo", "segunda-feira", "terça-feira", "quarta-feira", "quinta-feira", "sexta-feira", "sábado"};

    protected static final String[] MONTHS = new String[]{"janeiro", "fevereiro", "março", "abril", "maio", "junho", "julho", "agosto", "setembro", "outubro", "novembro", "dezembro"};


    public static boolean isNumeric(String strNum) {
        return strNum.matches("-?\\d+(\\.\\d+)?");
    }


    /**
     * Formata um número "#,###" através do {@link DecimalFormat}
     *
     * @param number Para formatar.
     * @return O número formatado.
     */
    public static String formatNumber(int number) {
        return DECIMAL_FORMAT.format(number);
    }

    /**
     * Formata um número "#,###" através do {@link DecimalFormat}
     *
     * @param number Para formatar.
     * @return O número formatado.
     */
    public static String formatNumber(long number) {
        return DECIMAL_FORMAT.format(number);
    }

    /**
     * Formata um número "#,###.##" através do {@link NumberFormat}
     *
     * @param number Para formatar.
     * @return O número formatado.
     */
    public static String formatLargeAndPreciseNumber(double number) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.forLanguageTag("en-US"));
        if (numberFormat.format(number).concat(" ").concat("").replace(" ", "").split("\\.").length > 1) {
            if (numberFormat.format(number).concat(" ").concat("").replace(" ", "").split("\\.")[1].length() == 3) {
                return numberFormat.format(number).replace(" ", "").substring(0, numberFormat.format(number).replace(" ", "").length() - 1).replace(".00", "");
            }
        } else {
            return numberFormat.format(number).concat(" ").concat("").replace(" ", "").replace(".00", "");
        }
        return numberFormat.format(number).concat(" ").concat("").replace(" ", "").replace(".00", "");
    }

    /**
     * Formata um número "#,###" através do {@link DecimalFormat}
     *
     * @param number Para formatar.
     * @return O número formatado.
     */
    public static String formatNumber(double number) {
        return DECIMAL_FORMAT.format(number);
    }

    private static final Pattern COLOR_PATTERN = Pattern.compile("(?i)(§)[0-9A-FK-OR]");

    /**
     * Remove todas as cores de uma String.<br/>
     * Color code: §
     *
     * @param input A string para remover as cores.
     * @return A string sem cores.
     */
    public static String stripColors(final String input) {
        if (input == null) {
            return null;
        }

        return COLOR_PATTERN.matcher(input).replaceAll("");
    }

    /**
     * Formata os {@code &} para o color code {@code §}.
     *
     * @param textToFormat A string para formatar as cores.
     * @return A string com as cores formatadas.
     */
    public static String formatColors(String textToFormat) {
        return translateAlternateColorCodes('&', textToFormat);
    }

    /**
     * Desformata o color code {@code §} para {@code &}.
     *
     * @param textToDeFormat A string para desformatar as cores.
     * @return A string com as cores desformatadas.
     */
    public static String deformatColors(String textToDeFormat) {
        Matcher matcher = COLOR_PATTERN.matcher(textToDeFormat);
        while (matcher.find()) {
            String color = matcher.group();
            textToDeFormat = textToDeFormat.replaceFirst(Pattern.quote(color), Matcher.quoteReplacement("&" + color.substring(1)));
        }

        return textToDeFormat;
    }

    public static String removeNumbers(String string) {
        return string.replaceAll("[0-9]", "");
    }


    /**
     * Formata os {@link altColorChar} para o color code {@code §}.
     *
     * @param altColorChar    O caractere que é definido como color code.
     * @param textToTranslate A string para formatar as cores.
     * @return A string com as cores formatadas.
     */


    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        Pattern pattern = Pattern.compile("(?i)(" + String.valueOf(altColorChar) + ")[0-9A-FK-OR]");

        Matcher matcher = pattern.matcher(textToTranslate);
        while (matcher.find()) {
            String color = matcher.group();
            textToTranslate = textToTranslate.replaceFirst(Pattern.quote(color), Matcher.quoteReplacement("§" + color.substring(1)));
        }

        return textToTranslate;
    }

    /**
     * Centraliza  {@link String} baseado em um tamanho x.
     *
     * @param s    Para centralizar.
     * @param size Tamanho do total na linha.
     * @return O texto no centro.
     */


    public static String center(String s, int size) {
        return center(s, size, ' ');
    }

    /**
     * Centraliza  {@link String} baseado em um tamanho x
     *
     * @param s    Para centralizar.
     * @param size Tamanho do total na linha.
     * @param pad  O caractere que vai ser colocado para reposicionar.
     * @return O texto no centro.
     */


    public static String center(String s, int size, char pad) {
        if (s == null || size <= s.length())
            return s;

        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < (size - s.length()) / 2; i++) {
            sb.append(pad);
        }
        sb.append(s);
        while (sb.length() < size) {
            sb.append(pad);
        }
        return sb.toString();
    }

    /**
     * Cria uma barra de progresso customizada de acordo com as porcentagem ditas.
     *
     * @param current        Valor atual.
     * @param max            Valor máximo, ou seja, o 100%.
     * @param doneColor      A cor no @link {@link ChatColor} que será colocada quando for dado como "concluído"
     * @param remainingColor A cor no @link {@link ChatColor} que será colocada quando for dado como "não concluído"
     * @param format         O formato de como ficará a barra de progresso onde {@code $bar=Barra em si} e {@code $progress=O processo em porcentagem}
     * @param amplifier      A quantidade máxima de barras.
     * @return A barra de progresso criada.
     */


    public static String progressBar(float current, float max, int amplifier, String character, ChatColor doneColor, ChatColor remainingColor, String format) {
        float done = (current / max) * 100;
        float remaining = 100 - done;

        if (current >= max) {
            done = 100;
            remaining = 0;
        }
        StringBuilder barFormat = new StringBuilder();

        int doneSquares = Math.round((done * amplifier) / 100);
        int remainingSquares = Math.round((remaining * amplifier) / 100);

        Collections.nCopies(doneSquares, (doneColor + character + "&r")).forEach(barFormat::append);
        Collections.nCopies(remainingSquares, (remainingColor + character + "&r")).forEach(barFormat::append);

        return translateAlternateColorCodes('&', format.replace("$bar", barFormat).replace("$progress", formatNumber(done)));
    }


    /**
     * Cria uma barra de progresso customizada de acordo com as porcentagem ditas.
     *
     * @param current   Valor atual.
     * @param max       Valor máximo, ou seja, o 100%.
     * @param amplifier A quantidade máxima de barras.
     * @return A barra de progresso criada.
     */


    public static String progressBar(float current, float max, int amplifier) {
        return progressBar(current, max, amplifier, "■", ChatColor.AQUA, ChatColor.GRAY, "&8[$bar&8]");
    }

    /**
     * Tranforma um número no format #.#K e etc.
     *
     * @param number O número a ser formatado.
     * @return O número no formato #.#K, #.#M e etc.
     */


    public static String formatNumberUS(double number) {
        String r = new DecimalFormat("##0E0").format(number);
        r = r.replaceAll("E[0-9]", suffix[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
        while (r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")) {
            r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1);
        }
        return r;
    }

    /**
     * Tranforma um número no format #.#K e etc, porém em outra alternativa.
     *
     * @param number O número a ser formatado.
     * @return O número no formato #.#K, #.#M e etc.
     */

    public static String formatNumberUSAlternative(double number) {
        List<String> numberRange = Lists.newArrayList(NUMBER_FORMAT);
        Object[] object = numberRange.stream().map(c -> new Object[]{c, (number / Math.pow(1000, numberRange.indexOf(c) + 1))}).filter(v -> (double) v[1] < 1000 & (double) v[1] > 0.1).findFirst().orElse(new Object[]{NUMBER_FORMAT[21], number / (Math.pow(1000, 22))});
        return number >= 1000 ? formatLargeAndPreciseNumber((Double) object[1]) + object[0] : formatLargeAndPreciseNumber(number);
    }


    /**
     * Pega a primeira cor de uma {@code String}.
     *
     * @param input A string para pegar a cor.
     * @return A primeira cor ou {@code ""(vazio)} caso não encontre nenhuma.
     */


    public static String getFirstColor(String input) {
        Matcher matcher = COLOR_PATTERN.matcher(input);
        String first = "";
        if (matcher.find()) {
            first = matcher.group();
        }

        return first;
    }

    /**
     * Pega a última cor de uma {@code String}.
     *
     * @param input A string para pegar a cor.
     * @return A última cor ou {@code ""(vazio)} caso não encontre nenhuma.
     */
    public static String getLastColor(String input) {
        Matcher matcher = COLOR_PATTERN.matcher(input);
        String last = "";
        while (matcher.find()) {
            last = matcher.group();
        }

        return last;
    }

    /**
     * Repete uma String várias vezes.
     *
     * @param repeat A string para repetir.
     * @param amount A quantidade de vezes que será repetida.
     * @return Resultado da repetição.
     */
    public static String repeat(String repeat, int amount) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            sb.append(repeat);
        }

        return sb.toString();
    }

    /**
     * Junta uma Array em uma {@code String} utilizando um separador.
     *
     * @param array     A array para juntar.
     * @param index     O ínicio da iteração da array (0 = toda a array).
     * @param separator O separador da junção.
     * @return Resultado da junção.
     */
    public static <T> String join(T[] array, int index, String separator) {
        StringBuilder joined = new StringBuilder();
        for (int slot = index; slot < array.length; slot++) {
            joined.append(array[slot].toString() + (slot + 1 == array.length ? "" : separator));
        }

        return joined.toString();
    }

    /**
     * Junta uma Array em uma {@code String} utilizando um separador.
     *
     * @param array     A array para juntar.
     * @param separator O separador da junção.
     * @return Resultado da junção.
     */
    public static <T> String join(T[] array, String separator) {
        return join(array, 0, separator);
    }

    /**
     * Junta uma Coleção em uma {@code String} utilizando um separador.
     *
     * @param collection A coleção para juntar.
     * @param separator  O separador da junção.
     * @return Resultado da junção.
     */
    public static <T> String join(Collection<T> collection, String separator) {
        return join(collection.toArray(new Object[collection.size()]), separator);
    }

    /**
     * Quebra uma {@code String} várias vezes para criar linhas com o tamanho máximo definido.<br/>
     * <b>Observação:</b> Esse método é uma variação do {@link StringUtils#split(String, int, boolean)}
     * com o parâmetro {@code ignoreCompleteWords} definido como {@code false}.
     *
     * @param toSplit String para quebrar.
     * @param length  Tamanho máximo das linhas.
     * @return Resultado da separação.
     */
    public static String[] split(String toSplit, int length) {
        return split(toSplit, length, false);
    }

    /**
     * "Capitaliza" uma String Exemplo: MAXTER se torna Maxter
     *
     * @param toCapitalise String para capitalizar
     * @return Resultado capitalizado.
     */
    public static String capitalise(String toCapitalise) {
        StringBuilder result = new StringBuilder();

        String[] splittedString = toCapitalise.split(" ");
        for (int index = 0; index < splittedString.length; index++) {
            String append = splittedString[index];
            result.append(append.substring(0, 1).toUpperCase() + append.substring(1).toLowerCase() + (index + 1 == splittedString.length ? "" : " "));
        }

        return result.toString();
    }


    /**
     * Quebra uma {@code String} várias vezes para criar linhas com o tamanho máximo definido.
     *
     * @param toSplit               String para quebrar.
     * @param length                Tamanho máximo das linhas.
     * @param ignoreIncompleteWords Se irá ignorar a quebra de palavras ou não (caso esteja desativado e
     *                              for quebrar uma palavra, ela irá ser removida da linha atual e adicionar na próxima).
     * @return Resultado da separação.
     */
    public static String[] split(String toSplit, int length, boolean ignoreIncompleteWords) {
        StringBuilder result = new StringBuilder(), current = new StringBuilder();

        char[] arr = toSplit.toCharArray();
        for (int charId = 0; charId < arr.length; charId++) {
            char character = arr[charId];
            if (current.length() == length) {
                if (!ignoreIncompleteWords) {
                    List<Character> removedChars = new ArrayList<>();
                    for (int l = current.length() - 1; l > 0; l--) {
                        if (current.charAt(l) == ' ') {
                            current.deleteCharAt(l);
                            result.append("§7").append(current.toString()).append("\n").append("§7");
                            Collections.reverse(removedChars);
                            current = new StringBuilder(join(removedChars, ""));
                            break;
                        }

                        removedChars.add(current.charAt(l));
                        current.deleteCharAt(l);
                    }

                    removedChars.clear();
                } else {
                    result.append(current.toString()).append("\n");
                    current = new StringBuilder();
                }
            }

            current.append(current.length() == 0 && character == ' ' ? "" : character);
            if (charId + 1 == arr.length) {
                result.append(current.toString() + "\n");
            }
        }

        return result.toString().split("\n");
    }

    /**
     * Cria um formato de data brasileiro. Ex: segunda, 20 de janeiro de 2021 às 11:10 da manhã.
     *
     * @param milliseconds O tempo em milisegundos a ser formatado.
     * @return Retorna a data formatada em padrão brasileiro.
     */

    public static String composeBrazilianBate(long milliseconds) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(milliseconds));
        String hour = String.valueOf((c.get(Calendar.HOUR_OF_DAY) > 12 ? (c.get(Calendar.HOUR_OF_DAY) - 12) : c.get(Calendar.HOUR_OF_DAY)));
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        StringBuilder sb = new StringBuilder();
        if (hourOfDay <= 5) {
            sb.append(c.get(Calendar.MINUTE)).append(" da madrugada");
        } else if (hourOfDay <= 12) {
            sb.append(c.get(Calendar.MINUTE)).append(" da manhã");
        } else if (hourOfDay < 18) {
            sb.append(c.get(Calendar.MINUTE)).append(" da tarde");
        } else {
            sb.append(c.get(Calendar.MINUTE)).append(" da noite");
        }
        String day_of_week = DAY_OF_WEEK[c.get(Calendar.DAY_OF_WEEK) - 1];
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String month = MONTHS[c.get(Calendar.MONTH)];
        String year = String.valueOf(c.get(Calendar.YEAR));

        return day_of_week + ", " + day + " de " + month + " de " + year + " às " + (hour.length() == 1 ? "0" + hour : hour) + ":" + (c.get(Calendar.MINUTE) < 10 ?
                "0" + sb :
                sb.toString());
    }

    /**
     * Cria um timer no formato ##m ##s
     *
     * @param time O tempo em segundos a ser formatado.
     * @return Tempo em segundos formatado.
     */

    public static String composeTimerFromSeconds(int time) {
        long minutes = time / 60;
        long seconds = time % 60;

        return time < 60 ? seconds + "s" : minutes == 0 ? "" : minutes + "m " + (seconds == 0 ? "" : seconds + "s");

    }
}
