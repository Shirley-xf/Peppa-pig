package utils;

import dao.DbConnection;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * The class Film info parser has parse method to parse the information in data.txt to database.
 * It also contains 2 static method to update data in the database. One is setCountry, the other is setYear.
 * Since it does not provide setActors, setDirectors, setDuration... if changing is need, changing the raw
 * material data.txt is the only way.
 *
 */
public class FilmInfoParser {
    private static final String DEFAULT_DATA_PATH = "data";
    private static String sContent;
    private static String sDataPath;
    private static File[] sTypes;
    private static List<String> sDirNameList = new LinkedList<>();

    private static final FileSystem fs = FileSystems.getDefault();

    public FilmInfoParser() {}

    private void readInfo(String path) {
        byte[] buffer;
        StringBuilder sb = new StringBuilder();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path))) {
            buffer = new byte[1000];
            int bytes_read;
            while ((bytes_read = bis.read(buffer, 0, buffer.length)) != -1) {
                sb.append(new String(Arrays.copyOfRange(buffer, 0, bytes_read)));
            }
        } catch(Exception e) {
            System.err.println(e);
        }
        sContent = sb.toString();
    }


    /**
     * Parse the information according to the path to grabbed data by the FilmInfoScrap.
     * <p>
     * The database should be set up adequately in dbres, for instance the columns and the tables.
     * And then all info in data.txt can be parsed to database. Besides, introductions will be exported
     * to the path to introduction
     * </p>
     *
     * @param path_to_data      path to data.txt that grabbed by FilmInfoScrap
     * @param path_to_intro_raw path to introduction
     */
    public void parse(String path_to_data, String path_to_intro_raw) {
        if (path_to_data.length() > 0) {
            sDataPath = path_to_data;
        } else {
            sDataPath = Paths.get(".").toAbsolutePath().normalize().toString() + fs.getSeparator() + DEFAULT_DATA_PATH;
        }

        String path_to_intro = (path_to_intro_raw.length() > 0) ? path_to_intro_raw : sDataPath + fs.getSeparator() + "introductions";
        readInfo(sDataPath + fs.getSeparator() +"data.txt");
        String sql;
        String[] all_infos = sContent.split("\n");
        File films_dir = new File(sDataPath + fs.getSeparator() +"films");
        sTypes = films_dir.listFiles(e -> e.getName().charAt(0) != '.');
        File intro_dir = new File(path_to_intro);
        File img_dir = new File(sDataPath + fs.getSeparator() +"pictures");
        FileOutputStream fos;
        sql = "delete from film_actor; delete from film_director; delete from film;";
        try {
            DbConnection.exeUpdate(sql);
        } catch (Exception e) {
            System.err.println("refreshing db: " + e);
        }

        for (String one_line : all_infos) {
            String[] field = one_line.split("\\|");

            String[] directors = field[1].split(",");
            String[] actors = field[2].split(",");

            try {
                File intro_file = new File(intro_dir, field[0] + ".txt");
                if (!intro_file.exists()) {
                    intro_file.createNewFile();
                }
                fos = new FileOutputStream(intro_file);
                fos.write(field[4].getBytes());
                fos.flush();



                sql = "insert into film (name, duration, intro_url) values (" +
                        "\"" + field[0] + "\", \"" + field[3] + "\", \"" + intro_file.toURI().toURL().toExternalForm() + "\");";
                try {
                    DbConnection.exeUpdate(sql);
                } catch (Exception e) {
                    System.err.println(e);
                }

                sql = "select id from film where name = \"" + field[0] + "\";";
                ResultSet id_res = DbConnection.query(sql);

                int id = id_res.getInt(1);
                int cnt = 0;

                for (String dirtr : directors) {
                    sql = "insert into film_director (id, director) values (" + id + ",\"" + dirtr + "\");";
                    try {
                        DbConnection.exeUpdate(sql);
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    if (++cnt > 1) break;
                }

                cnt = 0;
                for (String actr : actors) {
                    sql = "insert into film_actor (id, actor) values (" + id + ",\"" + actr + "\");";
                    try {
                        DbConnection.exeUpdate(sql);
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    if (++cnt > 2) break;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (File type_ : sTypes) {
            try {
                File[] films_in_dir = type_.listFiles(e -> e.getName().charAt(0) != '.');
                for (File film_ : films_in_dir) {
                    String film_media_name = film_.getName();
                    String film_name = film_media_name.substring(0, film_media_name.lastIndexOf("."));
                    try {
                        sql = "update film set type = \"" + type_.getName() + "\" where name = \"" + film_name + "\";";
                        DbConnection.exeUpdate(sql);
                        sql = "update film set media_url = \"" + film_.toURI().toURL().toExternalForm() + "\" where name = \"" + film_name + "\";";
                        DbConnection.exeUpdate(sql);
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            sDirNameList.add(type_.getName());
//            addToDirNameList(type_);
        }

        File[] imgs = img_dir.listFiles(e -> e.getName().charAt(0) != '.');
        for (File img : imgs) {
            try {
                String file_img_name = img.getName();
                sql = "update film set img_url = \"" + img.toURI().toURL().toExternalForm()
                        + "\" where name = \"" + file_img_name.substring(0, file_img_name.lastIndexOf(".")) + "\";";
                DbConnection.exeUpdate(sql);
            } catch (Exception e) {
                System.err.println(e);
            }
        }

    }

    /**
     * Getter of the directories name list
     *
     * @return the list of directories name
     */
    public static List<String> getDirNameList() {

        return sDirNameList;
    }

    /**
     * Update the year information of a film.
     *
     * @param name name of the film
     * @param year year that specified
     */
    public static void setYear(String name, int year) {
        String sql = "update film set year = " + year + " where name = \"" + name + "\";";
        try {
            DbConnection.exeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the country information of a film
     *
     * @param name    name of the film
     * @param country country that specified
     */
    public static void setCountry(String name, String country) {
        String sql = "update film set country = \"" + country + "\" where name = \"" + name + "\";";
        try {
            DbConnection.exeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * getter of the path of data
     *
     * @return data path
     */
    public static String getDataPath() {
        return sDataPath;
    }
}
