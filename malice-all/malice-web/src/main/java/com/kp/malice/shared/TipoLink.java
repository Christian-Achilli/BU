package com.kp.malice.shared;

public enum TipoLink {
		DOCUMENT, TUTORIAL_VIDEO, TUTORIAL_DOCUMENT;
		
		// the valueOfMethod
		public static TipoLink fromString(String s) {
			for (TipoLink enu : values()) {
				if (enu.name().equals(s)) {
					return enu;
				}
			}
			return null;
		}

		// the identifierMethod
		public String toString(TipoLink tipoLink) {
			return tipoLink.name();
		}
}
