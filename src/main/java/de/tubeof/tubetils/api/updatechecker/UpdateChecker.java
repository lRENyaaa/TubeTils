package de.tubeof.tubetils.api.updatechecker;

import de.tubeof.tubetils.api.updatechecker.enums.ApiMethode;
import org.bukkit.plugin.Plugin;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {

    private Integer resourceId;
    private Plugin plugin;
    private ApiMethode apiMethode;
    private Boolean isPremium;

    public UpdateChecker(Integer resourceId, Plugin plugin, ApiMethode apiMethode, Boolean isPremium) throws IOException {
        this.resourceId = resourceId;
        this.plugin = plugin;
        this.apiMethode = apiMethode;
        this.isPremium = isPremium;

        start();
    }

    private URL apiUrl;
    private String latestVersion;
    private Integer latestVersionId;
    private Boolean outdated;

    public void start() throws IOException {
        if(apiMethode.equals(ApiMethode.YOURGAMESPACE)) {
            apiUrl = new URL("https://api.pool.yourgamespace.com/v1/versioncheck/getLatestVersion.php?id=" + resourceId);

            HttpURLConnection urlConnection = (HttpURLConnection) getApiUrl().openConnection();
            urlConnection.setRequestProperty("User-Agent", "TubeApiBridgeConnector");
            urlConnection.setRequestProperty("Header-Token", "SD998FS0FG07");
            urlConnection.connect();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            bufferedReader.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            latestVersion = jsonObject.getString("name");
            if(latestVersion == null) {
                throw new NullPointerException("API returned NULL as value");
            }
            if(!isPremium) latestVersionId = jsonObject.getInt("id");

            if(!getLatestVersion().equalsIgnoreCase(plugin.getDescription().getVersion())) outdated = true;
            else outdated = false;
        }
        else if(apiMethode.equals(ApiMethode.SPIGOTMCORG)) {
            apiUrl = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId);

            HttpURLConnection urlConnection = (HttpURLConnection) getApiUrl().openConnection();
            urlConnection.connect();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            bufferedReader.close();

            latestVersion = inputLine;
            if(latestVersion == null) throw new NullPointerException("API returned NULL as value");

            if(!getLatestVersion().equalsIgnoreCase(plugin.getDescription().getVersion())) outdated = true;
            else outdated = false;
        } else {
            throw new NullPointerException("The defined API-Method could not be found!");
        }
    }

    /**
     *
     * @return Returns the API-URL which was used
     */
    public URL getApiUrl() {
        return apiUrl;
    }

    /**
     *
     * @return Returns the latest version string of the given plugin
     */
    public String getLatestVersion() {
        return latestVersion;
    }

    /**
     *
     * @return Returns the current installed version string of the given plugin
     */
    public String getCurrentVersion() {
        return plugin.getDescription().getVersion();
    }

    /**
     *
     * @return Returns true if the current version is not equal to the installed version of the given plugin
     */
    public Boolean isOutdated() {
        return outdated;
    }

    /**
     *
     * @return Returns the download URL for users. Useful for ingame/console notifications.
     */
    public String getDownloadUrl() {
        if(isPremium) return "https://www.spigotmc.org/resources/" + resourceId;
        if(latestVersionId == null) {
            return "https://www.spigotmc.org/resources/" + resourceId + "/history";
        } else {
            return "https://www.spigotmc.org/resources/" + resourceId + "/download?version=" + latestVersionId;
        }
    }
}
