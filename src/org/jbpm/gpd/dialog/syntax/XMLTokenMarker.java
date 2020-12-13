package org.jbpm.gpd.dialog.syntax;

/*
 * XMLTokenMarker.java - XML token marker
 * Copyright (C) 1999 Slava Pestov
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

/**
 * XML token marker.
 *
 * @author Slava Pestov
 * @version $Id: XMLTokenMarker.java,v 1.1 2006/02/14 17:37:43 mario Exp $
 */
public class XMLTokenMarker extends HTMLTokenMarker
{
	public XMLTokenMarker()
	{
		super(false);
	}
	public String toString(){
		return ("XML");
	}
}
