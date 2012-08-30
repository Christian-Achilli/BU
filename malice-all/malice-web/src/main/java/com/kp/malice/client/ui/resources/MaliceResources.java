package com.kp.malice.client.ui.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeUri;

public interface MaliceResources extends ClientBundle {

	public final static MaliceResources INSTANCE = GWT.create(MaliceResources.class);

	// -------- IMMAGINI ---------

	@Source("img/alleg.png")
	ImageResource alleg();

	@Source("img/asc.gif")
	ImageResource asc();

	@Source("img/bg.gif")
	ImageResource bg();

	@Source("img/check.png")
	ImageResource check();

	@Source("img/desc.gif")
	ImageResource desc();

	@Source("img/dwnBkg.jpg")
	ImageResource dwnBkg();

	@Source("img/form_input.gif")
	ImageResource form_input();

	@Source("img/head.jpg")
	ImageResource head();

	@Source("img/iconDownload.png")
	ImageResource iconDownload();

	@Source("img/iconDownloadHover.png")
	ImageResource iconDownloadHover();

	@Source("img/lloyds_logo.jpg")
	ImageResource lloyds_logo();

	@Source("img/note.png")
	ImageResource note();

	@Source("img/pulsCruscUn.png")
	ImageResource pulsCruscUn();

	@Source("img/searchBkg.png")
	ImageResource searchBkg();

	@Source("img/unchecked.png")
	ImageResource unchecked();

	@Source("img/person.jpeg")
	ImageResource person();

	@Source("img/agency.jpg")
	ImageResource agency();

	// delete search button
	@Source("img/delete_search.png")
	ImageResource deleteSearch();

	// status button
	@Source("img/green_botton.png")
	ImageResource greenBotton();

	@Source("img/grey_botton.png")
	ImageResource greyBotton();

	@Source("img/red_botton.png")
	ImageResource redBotton();

	@Source("img/yellow_botton.png")
	ImageResource yellowBotton();

	@Source("img/blue_botton.png")
	ImageResource blueBotton();

	// didascalia
	@Source("img/green_did.png")
	ImageResource greenDid();

	@Source("img/grey_did.png")
	ImageResource greyDid();

	@Source("img/red_did.png")
	ImageResource redDid();

	@Source("img/blue_did.png")
	ImageResource blueDid();

	@Source("img/yellow_did.png")
	ImageResource yellowDid();

	@Source("img/blue_did.png")
	ImageResource premiLordi();

	@Source("img/green_did.png")
	ImageResource provvigioni();

	@Source("img/iconTransparent.png")
	ImageResource iconTrapsarent();

	@Source("css/main.css")
	Main main();

	// pop up inserimento stato
	@Source("img/popBkg.png")
	ImageResource popBkg();

	@Source("img/green_botton_suspended.png")
	ImageResource greenSuspendedBotton();

	@Source("img/blue_botton_suspended.png")
	ImageResource blueSuspendedBotton();

	@Source("img/puntoEsclamativoGrigio.png")
	ImageResource puntoEsclamativoGrigio();

	@Source("img/grey_unchecked.png")
	ImageResource grey_unchecked();

	@Source("img/grey_checked.png")
	ImageResource grey_checked();

	@Source("img/green_unchecked.png")
	ImageResource green_unchecked();

	@Source("img/green_checked.png")
	ImageResource green_checked();

	@Source("img/blu_unchecked.png")
	ImageResource blu_unchecked();

	@Source("img/blu_checked.png")
	ImageResource blu_checked();

	@Source("img/yellow_unchecked.png")
	ImageResource yellow_unchecked();

	@Source("img/yellow_checked.png")
	ImageResource yellow_checked();

	@Source("img/logoBig.png")
	ImageResource logoBig();

	@Source("img/logoWeb.png")
	ImageResource logoWeb();

	@Source("img/mailIcon.png")
	ImageResource mailIcon();

	@Source("img/busta_nera.png")
	ImageResource mailIconBlack();

}
