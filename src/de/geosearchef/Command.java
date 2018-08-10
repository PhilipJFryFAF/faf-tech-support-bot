package de.geosearchef;

import lombok.Data;

@Data
public class Command {
	private final String type;
	private final String cmd;
	private final String response;
}
