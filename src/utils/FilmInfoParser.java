package utils;

import dao.DbConnection;
import javafx.scene.control.Button;

import java.io.*;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FilmInfoParser {
    private static final String DEFAULT_DATA_PATH = "data";
    private static String sContent;
    private static String sDataPath;
    private static File[] sTypes;
    private static List<Button> sButtonList;

    public FilmInfoParser(String path_to_dir) {
        sButtonList = new LinkedList<>();
        parse(path_to_dir);
    }
    public void readInfo(String path) {
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


    public void parse(String path_to_data) {
        if (path_to_data.length() > 0) {
            sDataPath = path_to_data;
        } else {
            sDataPath = DEFAULT_DATA_PATH;
        }
        readInfo(sDataPath + "/data.txt");
        String sql;
        String[] all_infos = sContent.split("\n");
        File films_dir = new File(sDataPath + "/films");
        sTypes = films_dir.listFiles(e -> e.getName().charAt(0) != '.');
        File intro_dir = new File(sDataPath + "/introductions");
        File img_dir = new File(sDataPath + "/pictures");
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
            addToTypeButtonList(type_);
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
    public void addToTypeButtonList(File type_) {
        Button btn = new Button(type_.getName());
        btn.setId(type_.getName().toLowerCase());
        sButtonList.add(btn);
    }

    public List<Button> getButtonList() {

        return sButtonList;
    }

    public String getDataPath() {
        return sDataPath;
    }
}
