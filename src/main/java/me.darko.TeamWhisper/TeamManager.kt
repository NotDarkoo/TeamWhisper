package me.darko.TeamWhisper

import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.io.IOException
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID
object TeamManager {
    private val teams = mutableMapOf<String, Team>()
    private lateinit var file: File
    private lateinit var config: FileConfiguration

    fun createTeam(name: String, owner: Player): Boolean {
        if (teams.containsKey(name)) {
            return false
        }
        teams[name] = Team(name, owner, config)
        saveTeam(name)
        return true
    }

    fun initialize(plugin: JavaPlugin) {
        file = File(plugin.dataFolder, "teams.yml")
        config = YamlConfiguration.loadConfiguration(file)
        loadTeams()
    }

    fun disbandTeam(name: String): Boolean {
        if (teams.remove(name) != null) {
            config.set("teams.$name", null)
            saveConfig()
            return true
        }
        return false
    }

    fun getTeam(name: String): Team? {
        return teams[name]
    }

    fun getTeamByPlayer(player: Player): Team? {
        return teams.values.find { it.isMember(player) }
    }

    fun invitePlayer(teamName: String, player: Player): Boolean {
        val team = teams[teamName] ?: return false
        team.invite(player)
        return true
    }

    fun acceptInvite(player: Player): Boolean {
        val team = teams.values.find { it.isInvited(player) } ?: return false
        team.addMember(player)
        Bukkit.getPlayer(team.ownerUUID)?.sendMessage("${ChatColor.GREEN}${player.name} has accepted your invite to the team ${ChatColor.BLUE}${team.name}${ChatColor.RESET}.")
        saveTeam(team.name)
        return true
    }

    fun saveTeam(name: String) {
        val team = teams[name] ?: return
        val teamConfig = config.createSection("teams.$name")
        teamConfig.set("name", team.name)
        teamConfig.set("owner", team.ownerUUID.toString())
        teamConfig.set("members", team.getMem().map { it.toString() })
        saveConfig()
    }

    private fun saveConfig() {
        try {
            config.save(file)
        } catch (e: IOException) {
            Bukkit.getLogger().warning("Failed to save teams to file: ${e.message}")
        }
    }

    fun saveTeams() {
        try {
            config.set("teams", null)
            teams.forEach { (name, team) ->
                val teamConfig = config.createSection("teams.$name")
                teamConfig.set("name", team.name)
                teamConfig.set("owner", team.ownerUUID.toString())
                teamConfig.set("members", team.getMem().map { it.toString() })
            }
            saveConfig()
        } catch (e: IOException) {
            Bukkit.getLogger().warning("Failed to save teams to file: ${e.message}")
        }
    }


    fun loadTeams() {
        if (file.exists()) {
            try {
                val section = config.getConfigurationSection("teams") ?: return
                Bukkit.getLogger().info("Loading teams from file...")
                section.getKeys(false).forEach { key ->
                    val teamSection = section.getConfigurationSection(key) ?: return@forEach
                    val name = teamSection.getString("name") ?: return@forEach
                    val ownerUUID = UUID.fromString(teamSection.getString("owner") ?: return@forEach)
                    val members = teamSection.getStringList("members").mapNotNull {
                        try {
                            UUID.fromString(it)
                        } catch (e: IllegalArgumentException) {
                            Bukkit.getLogger().warning("Invalid UUID in team data: $it")
                            null
                        }
                    }.toSet()
                    val owner = Bukkit.getPlayer(ownerUUID) ?: Bukkit.getOfflinePlayer(ownerUUID)
                    teams[name] = Team(name, owner, config).apply {
                        this.members.addAll(members)
                    }
                    Bukkit.getLogger().info("Loaded team: $name with owner: $ownerUUID and members: $members")
                }
            } catch (e: Exception) {
                Bukkit.getLogger().warning("Failed to load teams from file: ${e.message}")
            }
        }
    }



}