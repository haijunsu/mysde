package com.navy.socket;

import java.io.Serializable;

public class Message implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3847813866649039173L;

	private String command;
	private String description;

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String toString() {

		return "command=" + command + ", description=" + description;
	}

}
