package br.com.pontoclass.lab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.plugin.logging.Log;

import br.com.pontoclass.lab.util.Patterns;

class LogInfoImpl implements LogInfo {

	private Log	log;
	private Map<String, Short> killed = new HashMap<>();
	private Map<String, Short> killers = new HashMap<>();
	private static final String PATTERN = Patterns.KILLING;
	private List<String> players = new ArrayList<String>();
	private List<String> lines;

	public LogInfoImpl(Log log, List<String> lines) {
		this.log = log;
		this.lines = lines;
	}
	
	public void process() {
		Pattern pattern = Pattern.compile(PATTERN);
		for(String line: lines) {
			Matcher matcher = pattern.matcher(line);
			if(matcher.find()) {
				registerPlayer(matcher.group(4), matcher.group(6));
				registerKilled(matcher.group(6));
				registerKiller(matcher.group(4));
			}
		}
		log.info("KILLING STATISTICS");
		log.info("---------------------------------------------------------");
		if(players.size() > 0) {
			for(String player: players) {
				short killed = 0, wasKilled = 0;
				if(killers.containsKey(player)) {
					killed = killers.get(player);
				}
				if(this.killed.containsKey(player)) {
					wasKilled = this.killed.get(player);
				}
				log.info(String.format("PLAYER %s KILLED %d OTHER PLAYER(S) AND WAS KILLED %d TIME(S)", player, killed, wasKilled));
			}
		} else {
			log.info("NO KILLING WAS REGISTERED");
		}
		log.info("---------------------------------------------------------");
	}

	private void registerPlayer(String... players) {
		for(String player: players) {
			if(this.players.contains(player)) {
				continue;
			}
			this.players.add(player);
		}
	}

	private void registerKiller(String player) {
		if(killers.containsKey(player)) {
			killers.put(player, (short) (killers.remove(player)+1));
		} else {
			killers.put(player, Short.valueOf("1"));
		}
	}

	private void registerKilled(String player) {
		if(killed.containsKey(player)) {
			killed.put(player, (short) (killed.remove(player)+1));
		} else {
			killed.put(player, Short.valueOf("1"));
		}
	}

}
