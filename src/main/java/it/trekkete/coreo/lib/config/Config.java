package it.trekkete.coreo.lib.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.trekkete.coreo.lib.utils.Point3D;
import it.trekkete.coreo.ui.utils.ConfigEventKeys;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Config implements Serializable {

    private static Config instance;

    private Config() {
        support = new PropertyChangeSupport(this);

        atleti = new ArrayList<>();
        brani = new ArrayList<>();
    }

    public static Config getInstance() {
        if(instance == null) {
            synchronized (Config.class) {
                if (instance == null)
                    instance = new Config();
            }
        }
        return instance;
    }

    private final PropertyChangeSupport support;

    private List<Atleta> atleti;

    private List<Brano> brani;

    public static class Atleta {

        public enum Classe {
            TAMBURO,
            CHIARINA,
            SBANDIERATORE
        }

        private int id;

        private String name;

        private Classe classe;

        private List<Movimento> movimento;

        public static class Movimento {

            private int id;

            private int coreografia;

            private List<Point3D> coordinata;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getCoreografia() {
                return coreografia;
            }

            public void setCoreografia(int coreografia) {
                this.coreografia = coreografia;
            }

            public List<Point3D> getCoordinata() {
                return coordinata;
            }

            public void setCoordinata(List<Point3D> coordinata) {
                this.coordinata = coordinata;
            }
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Classe getClasse() {
            return classe;
        }

        public void setClasse(Classe classe) {
            this.classe = classe;
        }

        public List<Movimento> getMovimento() {
            return movimento;
        }

        public void setMovimento(List<Movimento> movimento) {
            this.movimento = movimento;
        }

        @Override
        public boolean equals(Object obj) {

            if (obj instanceof Atleta other) {

                return other.getId() == this.getId();
            }

            return false;
        }
    }

    public static class Brano {

        public enum Classe{
            AA,
            AAp,
            D,
            Dp,
            Z,
            V,
            V3,
            F
        }

        private int id;

        private String name;

        private Classe diffChiarine;

        private Classe diffTamburi;

        private int passi;

        private List<Coreografia> coreografia;

        public static class Coreografia {

            private int id;

            private int inizio;

            private int durata;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getInizio() {
                return inizio;
            }

            public void setInizio(int inizio) {
                this.inizio = inizio;
            }

            public int getDurata() {
                return durata;
            }

            public void setDurata(int durata) {
                this.durata = durata;
            }
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Classe getDiffChiarine() {
            return diffChiarine;
        }

        public void setDiffChiarine(Classe diffChiarine) {
            this.diffChiarine = diffChiarine;
        }

        public Classe getDiffTamburi() {
            return diffTamburi;
        }

        public void setDiffTamburi(Classe diffTamburi) {
            this.diffTamburi = diffTamburi;
        }

        public int getPassi() {
            return passi;
        }

        public void setPassi(int passi) {
            this.passi = passi;
        }

        public List<Coreografia> getCoreografia() {
            return coreografia;
        }

        public void setCoreografia(List<Coreografia> coreografia) {
            this.coreografia = coreografia;
        }

        @Override
        public boolean equals(Object obj) {

            if (obj instanceof Brano other) {

                return other.getId() == this.getId();
            }

            return false;
        }
    }

    public List<Atleta> getAtleti() {
        return atleti;
    }

    public void setAtleti(List<Atleta> atleti) {
        this.atleti = atleti;
    }

    public List<Brano> getBrani() {
        return brani;
    }

    public void setBrani(List<Brano> brani) {
        this.brani = brani;
    }

    public List<Brano.Coreografia> getCoreografie() {

        List<Brano.Coreografia> coreografie = new ArrayList<>();

        brani.forEach(brano -> coreografie.addAll(brano.getCoreografia()));

        return coreografie;
    }

    private static void checkConfig(String filename) {

        File config = new File("/var/coreomaker/" + filename + ".trk");
        if (!config.exists()) {
            try {
                config.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void save(Config config, String filename) throws IOException {

        OutputStreamWriter writer = null;

        try {

            checkConfig(filename);

            FileOutputStream stream = new FileOutputStream("/var/coreomaker/" + filename + ".trk");

            writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            gson.toJson(config, writer);

        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception exc) { }
            }
        }
    }

    public static Config load(String save) {
        return new Gson().fromJson(save, Config.class);
    }

    public void insert(Atleta atleta) {
        atleti.add(atleta);

        support.firePropertyChange(ConfigEventKeys.CHANGE_ATLETA, null, atleta);
    }

    public void insert(Brano brano) {
        brani.add(brano);

        support.firePropertyChange(ConfigEventKeys.CHANGE_BRANO, null, brano);
    }

    public void delete(Atleta atleta) {
        atleti.removeIf(atleta1 -> atleta1.getId() == atleta.getId());

        support.firePropertyChange(ConfigEventKeys.CHANGE_ATLETA, null, atleta);
    }

    public void delete(Brano brano) {
        brani.removeIf(brano1 -> brano1.getId() == brano.getId());

        support.firePropertyChange(ConfigEventKeys.CHANGE_BRANO, null, brano);
    }

    public int getNextFreeAID() {

        if (atleti == null || atleti.isEmpty()) {
            return 1;
        }

        int max = 0;
        for (Atleta atleta : atleti) {
            if (max < atleta.getId()) {
                max = atleta.getId();
            }
        }

        return max + 1;
    }

    public int getNextFreeBID() {

        if (brani == null || brani.isEmpty()) {
            return 1;
        }

        int max = 0;
        for (Brano brano : brani) {
            if (max < brano.getId()) {
                max = brano.getId();
            }
        }

        return max + 1;
    }

    public void add(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void remove(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }
}
