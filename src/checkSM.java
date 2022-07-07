import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class checkSM {
    public void read(int number_of_lines, String s){
        System.out.println(number_of_lines);
        ArrayList arrayList = new ArrayList();

        String searchDirectory = "C:\\monitor\\logs\\";
        String fileName = s+".log";
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("regex:.*" + fileName);
        Collection<Path> find = null;
        try {
            find = find(searchDirectory, matcher);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //     System.out.println(find);

        for (Path p: find){
            System.out.println("---> "+p.getFileName());
            try (BufferedReader reader = new BufferedReader(new FileReader(p.toFile()))) {
                //BufferedReader reader = new BufferedReader(new FileReader(p.toFile()));
                // считаем сначала первую строку
                String line = reader.readLine();
                while (line != null) {
                    arrayList.add(line);
                    line = reader.readLine();
                }
                int a_size = arrayList.size()-1;
                for (int i = a_size-number_of_lines;i<=a_size;i++){
                    System.out.println(arrayList.get(i));
                }
                System.out.println(s);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // построчное считывание файла
    public static void main(String[] args) {
        DateFormat df = new SimpleDateFormat("yyyy_MM_dd");
        int number_of_lines;
        String date="";
        checkSM checkSM =  new checkSM();

        if(args.length == 0){
            number_of_lines = 3;
            String requiredDate = df.format(new Date());
            date = requiredDate;
            checkSM.read(number_of_lines,date);
        } else if (args.length == 1) {
            number_of_lines = Integer.parseInt(args[0]);
            String requiredDate = df.format(new Date());
            date = requiredDate;
            checkSM.read(number_of_lines,date);
        } else if (args.length == 2) {
            number_of_lines = Integer.parseInt(args[0]);
            date = args[1];
            checkSM.read(number_of_lines,date);
        } else  {
            System.err.println("!!! incorrect systemax, try this -->   java jar checkSM.jar <number lines> <date yyyy_MM_dd>");
        }

    }

    protected static Collection<Path> find(String searchDirectory, PathMatcher matcher) throws IOException {
        try (Stream<Path> files = Files.walk(Paths.get(searchDirectory))) {
            return files
                    .filter(matcher::matches)
                    .collect(Collectors.toList());
        }
    }
}

