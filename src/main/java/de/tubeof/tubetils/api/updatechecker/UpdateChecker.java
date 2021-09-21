package de.tubeof.tubetils.api.updatechecker;

import de.tubeof.tubetils.api.updatechecker.enums.ApiMethode;
import de.tubeof.tubetils.data.Data;
import de.tubeof.tubetils.main.TubeTils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

@SuppressWarnings({"unused", "FieldCanBeLocal", "ConstantConditions"})
public class UpdateChecker {

    private final Data data = TubeTils.getData();
    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();

    private final Integer resourceId;
    private final Plugin plugin;
    private final ApiMethode apiMethode;
    private final Boolean isPremium;

    public UpdateChecker(String updateCheckerName, Integer resourceId, Plugin plugin, ApiMethode apiMethode, boolean isPremium, boolean autoCheck) throws IOException {
        if(data.isDebuggingEnabled()) ccs.sendMessage(TubeTils.getData().getPrefix() + "Created new UpdateChecker with name: " + updateCheckerName);

        this.resourceId = resourceId;
        this.plugin = plugin;
        this.apiMethode = apiMethode;
        this.isPremium = isPremium;

        if(autoCheck) check();
    }

    @Deprecated
    public UpdateChecker(Integer resourceId, Plugin plugin, ApiMethode apiMethode, boolean isPremium, boolean autoCheck) throws IOException {
        this.resourceId = resourceId;
        this.plugin = plugin;
        this.apiMethode = apiMethode;
        this.isPremium = isPremium;

        if(autoCheck) check();
    }

    private boolean isOnline = false;
    private boolean wasSuccessful = false;
    private boolean rateLimited = false;

    private URL apiUrl;
    private String latestVersion;
    private Integer latestVersionId;
    private boolean outdated;

    public void check() throws IOException {
        onlineCheck();
        if(!isOnline) {
            wasSuccessful = false;
            return;
        }

        if(apiMethode.equals(ApiMethode.YOURGAMESPACE)) {
            apiUrl = new URL("https://api.pool.yourgamespace.com/v1/versioncheck/getLatestVersion.php?id=" + resourceId);

            // Create connection
            HttpURLConnection urlConnection = (HttpURLConnection) getApiUrl().openConnection();
            urlConnection.setRequestProperty("User-Agent", "TubeTilsUpdateChecker");
            urlConnection.setRequestProperty("Header-Token", "SD998FS0FG07");
            urlConnection.setConnectTimeout(4000);
            urlConnection.setReadTimeout(4000);

            // Check status code
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == 429) {
                rateLimited = true;
                wasSuccessful = false;
                return;
            }
            else if(responseCode != 200) {
                wasSuccessful = false;
                return;
            }

            // Connect
            try {
                urlConnection.connect();
            } catch (SocketTimeoutException exception) {
                wasSuccessful = false;
                return;
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
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

            outdated = !getLatestVersion().equalsIgnoreCase(plugin.getDescription().getVersion());

            wasSuccessful = true;
        }
        else if(apiMethode.equals(ApiMethode.SPIGOTMCORG)) {
            apiUrl = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId);

            HttpURLConnection urlConnection = (HttpURLConnection) getApiUrl().openConnection();
            urlConnection.setRequestProperty("User-Agent", "TubeTilsUpdateChecker");
            urlConnection.setRequestProperty("Header-Token", "SD998FS0FG07");
            urlConnection.setConnectTimeout(4000);
            urlConnection.setReadTimeout(4000);

            // Check status code
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == 429) {
                rateLimited = true;
                wasSuccessful = false;
                return;
            }
            else if(responseCode != 200) {
                wasSuccessful = false;
                return;
            }

            // Connect
            try {
                urlConnection.connect();
            } catch (SocketTimeoutException exception) {
                wasSuccessful = false;
                return;
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            bufferedReader.close();

            latestVersion = response.toString();
            if(latestVersion == null) throw new NullPointerException("API returned NULL as value");

            outdated = !getLatestVersion().equalsIgnoreCase(plugin.getDescription().getVersion());

            wasSuccessful = true;
        } else {
            throw new NullPointerException("The defined API-Method could not be found!");
        }
    }

    private void onlineCheck() {
        boolean amionline = false;
        boolean yourgamespace = false;

        try {
            URL url = new URL("http://amionline.net/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "TubeTilsManagerCheck");
            connection.setRequestProperty("Header-Token", "SD998FS0FG07");
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            amionline = true;
        } catch (IOException exception) {
            // Just catch
        }

        try {
            URL url = new URL("https://api.yourgamespace.com/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "TubeTilsManagerCheck");
            connection.setRequestProperty("Header-Token", "SD998FS0FG07");
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            yourgamespace = true;
        } catch (IOException exception) {
            // Just catch
        }

        isOnline = amionline || yourgamespace;
    }

    /**
     * 
     * @return Returns the status
     */
    public boolean wasSuccessful() {
        return wasSuccessful;
    }

    /**
     * 
     * @return Returns true, if the API Call was blocked due rate limit
     */
    public boolean isRateLimited() {
        return rateLimited;
    }

    /**
     * 
     * @return Returns the status of the online check
     */
    public boolean isOnline() {
        return isOnline;
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
