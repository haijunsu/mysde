package com.navy.docman.hibernate;


public enum HibernateEnum implements SqlTypeEnum<HibernateEnum> {

	UNKNOW(0) {
		public String toString() {
			return "UNKNOW";
		}
	};

	protected int value;

	private HibernateEnum(int value) {
		this.value = value;
	}

	public Enum<HibernateEnum> fromValue(int value) {
		switch (value) {
		case 0:
			return UNKNOW;
		default:
			return null;
		}
	}

	public int value() {
		return value;
	}

}
