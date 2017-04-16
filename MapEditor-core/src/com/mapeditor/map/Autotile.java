package com.mapeditor.map;

import java.io.IOException;
import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mapeditor.game.MapEditor;
import com.mapeditor.screen.ScreenEditor;

/**
 * Un Autotile est un carreau liable. Selon la nature de ses voisins, il prendra
 * une apparence diff�rente. Un Autotile peut �tre anim�. Il est alors constitu�
 * de plusieurs vignettes qui se succ�deront dans le temps.
 */
public class Autotile {
	// Constantes

	private static final int TAILLE_D_UN_CARREAU = 16;
	/** fr�quence d'animation des autotiles anim�s (eau, etc.) */
	public static final int FREQUENCE_ANIMATION_AUTOTILE = 10;
	/** largeur standard pour une image d'Autotile fixe */
	public static final int LARGEUR_AUTOTILE_FIXE = 3 * TAILLE_D_UN_CARREAU;
	/** nombre de vignettes qui composent l'animation d'un Autotile */
	public static final int NOMBRE_VIGNETTES_AUTOTILE_ANIME = 4;
	/** largeur standard pour une image d'Autotile anim� */
	public static final int LARGEUR_AUTOTILE_ANIME = NOMBRE_VIGNETTES_AUTOTILE_ANIME * LARGEUR_AUTOTILE_FIXE;
	/** hauteur standard pour une image d'Autotile */
	public static final int HAUTEUR_AUTOTILE = 4 * TAILLE_D_UN_CARREAU;
	/** l'Autotile est compos� de 4 quarts */
	public static final int TAILLE_MORCEAU = TAILLE_D_UN_CARREAU / 2;
	/**
	 * d�calage pour aller piocher dans la vignette d'animation suivante (en
	 * nombre de quarts de carreaux)
	 */
	public static final int DECALAGE_VIGNETTE_SUIVANTE = 6;

	// quart haut gauche
	public static final int X_PLEIN_HAUT_GAUCHE = 2;
	public static final int Y_PLEIN_HAUT_GAUCHE = 4;
	public static final int X_PLEIN_HAUT_GAUCHE_VIDE_A_DROITE = 4;
	public static final int Y_PLEIN_HAUT_GAUCHE_VIDE_A_DROITE = 4;
	public static final int X_PLEIN_HAUT_GAUCHE_VIDE_EN_BAS = 2;
	public static final int Y_PLEIN_HAUT_GAUCHE_VIDE_EN_BAS = 3;//
	public static final int X_PLEIN_HAUT_GAUCHE_VIDE_EN_BAS_ET_A_DROITE = 4;
	public static final int Y_PLEIN_HAUT_GAUCHE_VIDE_EN_BAS_ET_A_DROITE = 3;//
	public static final int X_COIN_RENTRANT_HAUT_GAUCHE = 4;
	public static final int Y_COIN_RENTRANT_HAUT_GAUCHE = 0;
	public static final int X_BORD_VERTICAL_HAUT_GAUCHE_LOIN_D_UN_COIN = 0;
	public static final int Y_BORD_VERTICAL_HAUT_GAUCHE_LOIN_D_UN_COIN = 4;
	public static final int X_BORD_VERTICAL_HAUT_GAUCHE_PRES_D_UN_COIN = 0;
	public static final int Y_BORD_VERTICAL_HAUT_GAUCHE_PRES_D_UN_COIN = 3;//
	public static final int X_BORD_HORIZONTAL_HAUT_GAUCHE_LOIN_D_UN_COIN = 2;
	public static final int Y_BORD_HORIZONTAL_HAUT_GAUCHE_LOIN_D_UN_COIN = 7;//
	public static final int X_BORD_HORIZONTAL_HAUT_GAUCHE_PRES_D_UN_COIN = 4;
	public static final int Y_BORD_HORIZONTAL_HAUT_GAUCHE_PRES_D_UN_COIN = 7;//
	public static final int X_COIN_SORTANT_HAUT_GAUCHE = 0;
	public static final int Y_COIN_SORTANT_HAUT_GAUCHE = 7;//

	// quart haut droite
	public static final int X_PLEIN_HAUT_DROITE = 3;
	public static final int Y_PLEIN_HAUT_DROITE = 4;
	public static final int X_PLEIN_HAUT_DROITE_VIDE_A_GAUCHE = 1;
	public static final int Y_PLEIN_HAUT_DROITE_VIDE_A_GAUCHE = 4;
	public static final int X_PLEIN_HAUT_DROITE_VIDE_EN_BAS = 3;
	public static final int Y_PLEIN_HAUT_DROITE_VIDE_EN_BAS = 3;//
	public static final int X_PLEIN_HAUT_DROITE_VIDE_EN_BAS_ET_A_GAUCHE = 1;
	public static final int Y_PLEIN_HAUT_DROITE_VIDE_EN_BAS_ET_A_GAUCHE = 3;//
	public static final int X_COIN_RENTRANT_HAUT_DROITE = 5;
	public static final int Y_COIN_RENTRANT_HAUT_DROITE = 0;
	public static final int X_BORD_VERTICAL_HAUT_DROITE_LOIN_D_UN_COIN = 5;
	public static final int Y_BORD_VERTICAL_HAUT_DROITE_LOIN_D_UN_COIN = 4;
	public static final int X_BORD_VERTICAL_HAUT_DROITE_PRES_D_UN_COIN = 5;
	public static final int Y_BORD_VERTICAL_HAUT_DROITE_PRES_D_UN_COIN = 3;//
	public static final int X_BORD_HORIZONTAL_HAUT_DROITE_LOIN_D_UN_COIN = 3;
	public static final int Y_BORD_HORIZONTAL_HAUT_DROITE_LOIN_D_UN_COIN = 7;//
	public static final int X_BORD_HORIZONTAL_HAUT_DROITE_PRES_D_UN_COIN = 1;
	public static final int Y_BORD_HORIZONTAL_HAUT_DROITE_PRES_D_UN_COIN = 7;//
	public static final int X_COIN_SORTANT_HAUT_DROITE = 5;
	public static final int Y_COIN_SORTANT_HAUT_DROITE = 7;//

	// quart bas gauche
	public static final int X_PLEIN_BAS_GAUCHE = 2;
	public static final int Y_PLEIN_BAS_GAUCHE = 5;
	public static final int X_PLEIN_BAS_GAUCHE_VIDE_A_DROITE = 4;
	public static final int Y_PLEIN_BAS_GAUCHE_VIDE_A_DROITE = 5;
	public static final int X_PLEIN_BAS_GAUCHE_VIDE_EN_HAUT = 2;
	public static final int Y_PLEIN_BAS_GAUCHE_VIDE_EN_HAUT = 6;//
	public static final int X_PLEIN_BAS_GAUCHE_VIDE_EN_HAUT_ET_A_DROITE = 4;
	public static final int Y_PLEIN_BAS_GAUCHE_VIDE_EN_HAUT_ET_A_DROITE = 6;//
	public static final int X_COIN_RENTRANT_BAS_GAUCHE = 4;
	public static final int Y_COIN_RENTRANT_BAS_GAUCHE = 1;
	public static final int X_BORD_VERTICAL_BAS_GAUCHE_LOIN_D_UN_COIN = 0;
	public static final int Y_BORD_VERTICAL_BAS_GAUCHE_LOIN_D_UN_COIN = 5;
	public static final int X_BORD_VERTICAL_BAS_GAUCHE_PRES_D_UN_COIN = 0;
	public static final int Y_BORD_VERTICAL_BAS_GAUCHE_PRES_D_UN_COIN = 6;//
	public static final int X_BORD_HORIZONTAL_BAS_GAUCHE_LOIN_D_UN_COIN = 2;
	public static final int Y_BORD_HORIZONTAL_BAS_GAUCHE_LOIN_D_UN_COIN = 2;//
	public static final int X_BORD_HORIZONTAL_BAS_GAUCHE_PRES_D_UN_COIN = 4;
	public static final int Y_BORD_HORIZONTAL_BAS_GAUCHE_PRES_D_UN_COIN = 2;//
	public static final int X_COIN_SORTANT_BAS_GAUCHE = 0;
	public static final int Y_COIN_SORTANT_BAS_GAUCHE = 2;//

	// quart bas droite
	public static final int X_PLEIN_BAS_DROITE = 3;
	public static final int Y_PLEIN_BAS_DROITE = 5;
	public static final int X_PLEIN_BAS_DROITE_VIDE_A_GAUCHE = 1;
	public static final int Y_PLEIN_BAS_DROITE_VIDE_A_GAUCHE = 5;
	public static final int X_PLEIN_BAS_DROITE_VIDE_EN_HAUT = 3;
	public static final int Y_PLEIN_BAS_DROITE_VIDE_EN_HAUT = 6;//
	public static final int X_PLEIN_BAS_DROITE_VIDE_EN_HAUT_ET_A_GAUCHE = 1;
	public static final int Y_PLEIN_BAS_DROITE_VIDE_EN_HAUT_ET_A_GAUCHE = 6;//
	public static final int X_COIN_RENTRANT_BAS_DROITE = 5;
	public static final int Y_COIN_RENTRANT_BAS_DROITE = 1;
	public static final int X_BORD_VERTICAL_BAS_DROITE_LOIN_D_UN_COIN = 5;
	public static final int Y_BORD_VERTICAL_BAS_DROITE_LOIN_D_UN_COIN = 5;
	public static final int X_BORD_VERTICAL_BAS_DROITE_PRES_D_UN_COIN = 5;
	public static final int Y_BORD_VERTICAL_BAS_DROITE_PRES_D_UN_COIN = 6;//
	public static final int X_BORD_HORIZONTAL_BAS_DROITE_LOIN_D_UN_COIN = 3;
	public static final int Y_BORD_HORIZONTAL_BAS_DROITE_LOIN_D_UN_COIN = 2;//
	public static final int X_BORD_HORIZONTAL_BAS_DROITE_PRES_D_UN_COIN = 1;
	public static final int Y_BORD_HORIZONTAL_BAS_DROITE_PRES_D_UN_COIN = 2;//
	public static final int X_COIN_SORTANT_BAS_DROITE = 5;
	public static final int Y_COIN_SORTANT_BAS_DROITE = 2;//

	private int numero;
	private Tileset tileset;
	public String nomImage;
	public boolean anime;
	public ArrayList<Integer> cousins;
	private Texture[][][] textures;
	private Pixmap pix;
	private Texture texTest;

	/**
	 * Constructeur explicite
	 * 
	 * @param numero
	 *            de l'Autotile connu par le Tileset
	 * @param nomImage
	 *            nom de l'image de l'Autotile
	 * @param passabilite
	 *            peut-on marcher sur cette case ?
	 * @param altitude
	 *            d'affichage dans le d�cor
	 * @param terrain
	 *            sp�cial qui peut avoir des propri�t�s particuli�res
	 * @param cousins
	 *            autres autotiles qui peuvent se lier � celui-ci
	 * @param tileset
	 *            auquel appartient cet Autotile
	 * @throws IOException
	 *             impossible de charger l'image de l'Autotile
	 */
	public Autotile(String string, int i) {
		this.numero = i;

		this.nomImage = string;

		pix = new Pixmap(Files.get("/autotiles/" + string + ".png"));

		final int largeurAutotile = pix.getWidth();
		if (largeurAutotile == LARGEUR_AUTOTILE_FIXE) {
			this.anime = false;
		} else if (largeurAutotile == LARGEUR_AUTOTILE_ANIME) {
			this.anime = true;
		}

		cousins = new ArrayList<Integer>();

		textures = new Texture[30][16][0];

	}

	/**
	 * Calculer l'apparence du carreau liable en fonction de son voisinnage.
	 * 
	 * @param x
	 *            coordonnee x de la case sur la Map (en nombre de carreaux)
	 * @param y
	 *            coordonnee y de la case sur la Map (en nombre de carreaux)
	 * @param largeurMap
	 *            largeur de la Map (en nombre de carreaux)
	 * @param hauteurMap
	 *            hauteur de la Map (en nombre de carreaux)
	 * @param numeroCarreau
	 *            num�ro de ce carreau de d�cor issu du Tileset
	 * @param layer
	 *            une des trois couches de d�cor de l'�diteur de Maps
	 * @return carreau liable avec la bonne apparence
	 */
	public final Texture[] calculerAutotile(final int x, final int y, final int largeurMap, final int hauteurMap,
			final int numeroCarreau, final int[][] layer) {
		// On veut d�terminer les connexions du carreau
		boolean connexionBas = false;
		boolean connexionGauche = false;
		boolean connexionDroite = false;
		boolean connexionHaut = false;
		boolean connexionBasGauche = false;
		boolean connexionBasDroite = false;
		boolean connexionHautGauche = false;
		boolean connexionHautDroite = false;

		// On consid�re que le bord de l'�cran est liable lui aussi
		if (y == 0) {
			// bord sup�rieur de l'�cran
			connexionHaut = true;
			connexionHautGauche = true;
			connexionHautDroite = true;
		} else if (y == hauteurMap - 1) {
			// bord inf�rieur de l'�cran
			connexionBas = true;
			connexionBasGauche = true;
			connexionBasDroite = true;
		}
		if (x == 0) {
			// bord gauche de l'�cran
			connexionGauche = true;
			connexionHautGauche = true;
			connexionBasGauche = true;
		} else if (x == largeurMap - 1) {
			// bord droit de l'�cran
			connexionDroite = true;
			connexionHautDroite = true;
			connexionBasDroite = true;
		}

		// On regarde les connexions possibles avec les carreaux voisins
		int numeroVoisin;
		if (!connexionHaut) {
			numeroVoisin = layer[x][y - 1];
			connexionHaut = fautIlLierCeCarreauASonVoisin(numeroCarreau, numeroVoisin);
		}
		if (!connexionBas) {
			numeroVoisin = layer[x][y + 1];
			connexionBas = fautIlLierCeCarreauASonVoisin(numeroCarreau, numeroVoisin);
		}
		if (!connexionGauche) {
			numeroVoisin = layer[x - 1][y];
			connexionGauche = fautIlLierCeCarreauASonVoisin(numeroCarreau, numeroVoisin);
		}
		if (!connexionDroite) {
			numeroVoisin = layer[x + 1][y];
			connexionDroite = fautIlLierCeCarreauASonVoisin(numeroCarreau, numeroVoisin);
		}

		// Selon les cas, ceux-l� ne sont pas forc�ment utiles pour dessiner le
		// carreau
		if (!connexionHautGauche) {
			numeroVoisin = layer[x - 1][y - 1];
			connexionHautGauche = fautIlLierCeCarreauASonVoisin(numeroCarreau, numeroVoisin);
		}
		if (!connexionHautDroite) {
			numeroVoisin = layer[x + 1][y - 1];
			connexionHautDroite = fautIlLierCeCarreauASonVoisin(numeroCarreau, numeroVoisin);
		}
		if (!connexionBasGauche) {
			numeroVoisin = layer[x - 1][y + 1];
			connexionBasGauche = fautIlLierCeCarreauASonVoisin(numeroCarreau, numeroVoisin);
		}
		if (!connexionBasDroite) {
			numeroVoisin = layer[x + 1][y + 1];
			connexionBasDroite = fautIlLierCeCarreauASonVoisin(numeroCarreau, numeroVoisin);
		}

		// choix de l'apparence de chaque quart du carreau
		final int[] morceauChoisi1 = choisirLeQuartHautGaucheDuCarreau(connexionHaut, connexionGauche,
				connexionHautGauche, connexionDroite, connexionBas);
		final int[] morceauChoisi2 = choisirLeQuartHautDroiteDuCarreau(connexionHaut, connexionDroite,
				connexionHautDroite, connexionGauche, connexionBas);
		final int[] morceauChoisi3 = choisirLeQuartBasGaucheDuCarreau(connexionBas, connexionGauche, connexionBasGauche,
				connexionDroite, connexionHaut);
		final int[] morceauChoisi4 = choisirLeQuartBasDroiteDuCarreau(connexionBas, connexionDroite, connexionBasDroite,
				connexionGauche, connexionHaut);

		// fabrication du carreau en dessinant chaque quart
		final Texture[] resultats = new Texture[NOMBRE_VIGNETTES_AUTOTILE_ANIME];
		for (int i = 0; (this.anime && i < NOMBRE_VIGNETTES_AUTOTILE_ANIME) || (!this.anime && i <= 0); i++) {
			// final Texture resultat = new BufferedImage(TAILLE_D_UN_CARREAU,
			// TAILLE_D_UN_CARREAU, 0);
			Texture resultat;
			Pixmap p = new Pixmap(TAILLE_D_UN_CARREAU, TAILLE_D_UN_CARREAU, Pixmap.Format.RGBA8888);
			// final Graphics2D g2d = (Graphics2D) resultat.createGraphics();

			// Pixmap p2 = new Pixmap(texture);

			p.drawPixmap(pix, 0, TAILLE_MORCEAU, morceauChoisi1[0] * TAILLE_MORCEAU, morceauChoisi1[1] * TAILLE_MORCEAU,
					TAILLE_MORCEAU, TAILLE_MORCEAU);

			p.drawPixmap(pix, TAILLE_MORCEAU, TAILLE_MORCEAU, morceauChoisi2[0] * TAILLE_MORCEAU,
					morceauChoisi2[1] * TAILLE_MORCEAU, TAILLE_MORCEAU, TAILLE_MORCEAU);

			p.drawPixmap(pix, 0, 0, morceauChoisi3[0] * TAILLE_MORCEAU, morceauChoisi3[1] * TAILLE_MORCEAU,
					TAILLE_MORCEAU, TAILLE_MORCEAU);

			p.drawPixmap(pix, TAILLE_MORCEAU, 0, morceauChoisi4[0] * TAILLE_MORCEAU, morceauChoisi4[1] * TAILLE_MORCEAU,
					TAILLE_MORCEAU, TAILLE_MORCEAU);

			resultat = new Texture(p);
			resultats[i] = resultat;

			// pr�paration du tour de boucle suivant : on peint la vignette
			// suivante
			morceauChoisi1[0] += DECALAGE_VIGNETTE_SUIVANTE;
			morceauChoisi2[0] += DECALAGE_VIGNETTE_SUIVANTE;
			morceauChoisi3[0] += DECALAGE_VIGNETTE_SUIVANTE;
			morceauChoisi4[0] += DECALAGE_VIGNETTE_SUIVANTE;
		}

		return resultats;
	}

	/**
	 * Est-ce que le carreau Autotile est � lier � ce voisin ?
	 * 
	 * @param numeroCarreau
	 *            num�ro du carreau actuel selon le Tileset
	 * @param numeroVoisin
	 *            num�ro du carreau voisin selon le Tileset
	 * @return true si connexion, false si aucun lien de parent�
	 */
	private boolean fautIlLierCeCarreauASonVoisin(final int numeroCarreau, final int numeroVoisin) {
		return (numeroVoisin == numeroCarreau) || this.cousins.contains(numeroVoisin)
				|| (numeroVoisin < -1 && this.tileset.autotiles[numeroVoisin].cousins.contains(this.numero));
	}

	/**
	 * Choisir l'apparence du quart haut-gauche du carreau Autotile.
	 * 
	 * @param connexionHaut
	 *            le carreau Autotile est-il connect� � un voisin ?
	 * @param connexionGauche
	 *            le carreau Autotile est-il connect� � un voisin ?
	 * @param connexionHautGauche
	 *            le carreau Autotile est-il connect� � un voisin ?
	 * @param connexionDroite
	 *            le carreau Autotile est-il connect� � un voisin ?
	 * @param connexionBas
	 *            le carreau Autotile est-il connect� � un voisin ?
	 * @return coordonn�es (en quarts de carreaux) du morceau d'Autotile �
	 *         peindre sur le carreau
	 */
	private int[] choisirLeQuartHautGaucheDuCarreau(final boolean connexionHaut, final boolean connexionGauche,
			final boolean connexionHautGauche, final boolean connexionDroite, final boolean connexionBas) {
		final int xMorceauChoisi;
		final int yMorceauChoisi;
		if (connexionHaut) {
			if (connexionGauche) {
				if (connexionHautGauche) {
					if (connexionDroite) {
						if (connexionBas) {
							xMorceauChoisi = X_PLEIN_HAUT_GAUCHE;
							yMorceauChoisi = Y_PLEIN_HAUT_GAUCHE;
						} else {
							xMorceauChoisi = X_PLEIN_HAUT_GAUCHE_VIDE_A_DROITE;
							yMorceauChoisi = Y_PLEIN_HAUT_GAUCHE_VIDE_A_DROITE;
						}
					} else {
						if (connexionBas) {
							xMorceauChoisi = X_PLEIN_HAUT_GAUCHE_VIDE_EN_BAS;
							yMorceauChoisi = Y_PLEIN_HAUT_GAUCHE_VIDE_EN_BAS;
						} else {
							xMorceauChoisi = X_PLEIN_HAUT_GAUCHE_VIDE_EN_BAS_ET_A_DROITE;
							yMorceauChoisi = Y_PLEIN_HAUT_GAUCHE_VIDE_EN_BAS_ET_A_DROITE;
						}
					}
				} else {
					xMorceauChoisi = X_COIN_RENTRANT_HAUT_GAUCHE;
					yMorceauChoisi = Y_COIN_RENTRANT_HAUT_GAUCHE;
				}
			} else {
				if (connexionBas) {
					xMorceauChoisi = X_BORD_VERTICAL_HAUT_GAUCHE_LOIN_D_UN_COIN;
					yMorceauChoisi = Y_BORD_VERTICAL_HAUT_GAUCHE_LOIN_D_UN_COIN;
				} else {
					xMorceauChoisi = X_BORD_VERTICAL_HAUT_GAUCHE_PRES_D_UN_COIN;
					yMorceauChoisi = Y_BORD_VERTICAL_HAUT_GAUCHE_PRES_D_UN_COIN;
				}

			}
		} else {
			if (connexionGauche) {
				if (connexionDroite) {
					xMorceauChoisi = X_BORD_HORIZONTAL_HAUT_GAUCHE_LOIN_D_UN_COIN;
					yMorceauChoisi = Y_BORD_HORIZONTAL_HAUT_GAUCHE_LOIN_D_UN_COIN;
				} else {
					xMorceauChoisi = X_BORD_HORIZONTAL_HAUT_GAUCHE_PRES_D_UN_COIN;
					yMorceauChoisi = Y_BORD_HORIZONTAL_HAUT_GAUCHE_PRES_D_UN_COIN;
				}

			} else {
				xMorceauChoisi = X_COIN_SORTANT_HAUT_GAUCHE;
				yMorceauChoisi = Y_COIN_SORTANT_HAUT_GAUCHE;
			}
		}
		return new int[] { xMorceauChoisi, yMorceauChoisi };
	}

	/**
	 * Choisir l'apparence du quart haut-droite du carreau Autotile.
	 * 
	 * @param connexionHaut
	 *            le carreau Autotile est-il connect� � un voisin ?
	 * @param connexionDroite
	 *            le carreau Autotile est-il connect� � un voisin ?
	 * @param connexionHautDroite
	 *            le carreau Autotile est-il connect� � un voisin ?
	 * @param connexionGauche
	 *            le carreau Autotile est-il connect� � un voisin ?
	 * @param connexionBas
	 *            le carreau Autotile est-il connect� � un voisin ?
	 * @return coordonn�es (en quarts de carreaux) du morceau d'Autotile �
	 *         peindre sur le carreau
	 */
	private int[] choisirLeQuartHautDroiteDuCarreau(final boolean connexionHaut, final boolean connexionDroite,
			final boolean connexionHautDroite, final boolean connexionGauche, final boolean connexionBas) {
		final int xMorceauChoisi;
		final int yMorceauChoisi;
		if (connexionHaut) {
			if (connexionDroite) {
				if (connexionHautDroite) {
					if (connexionGauche) {
						if (connexionBas) {
							xMorceauChoisi = X_PLEIN_HAUT_DROITE;
							yMorceauChoisi = Y_PLEIN_HAUT_DROITE;
						} else {
							xMorceauChoisi = X_PLEIN_HAUT_DROITE_VIDE_EN_BAS;
							yMorceauChoisi = Y_PLEIN_HAUT_DROITE_VIDE_EN_BAS;
						}
					} else {
						if (connexionBas) {
							xMorceauChoisi = X_PLEIN_HAUT_DROITE_VIDE_A_GAUCHE;
							yMorceauChoisi = Y_PLEIN_HAUT_DROITE_VIDE_A_GAUCHE;
						} else {
							xMorceauChoisi = X_PLEIN_HAUT_DROITE_VIDE_EN_BAS_ET_A_GAUCHE;
							yMorceauChoisi = Y_PLEIN_HAUT_DROITE_VIDE_EN_BAS_ET_A_GAUCHE;
						}
					}
				} else {
					xMorceauChoisi = X_COIN_RENTRANT_HAUT_DROITE;
					yMorceauChoisi = Y_COIN_RENTRANT_HAUT_DROITE;
				}
			} else {
				if (connexionBas) {
					xMorceauChoisi = X_BORD_VERTICAL_HAUT_DROITE_LOIN_D_UN_COIN;
					yMorceauChoisi = Y_BORD_VERTICAL_HAUT_DROITE_LOIN_D_UN_COIN;
				} else {
					xMorceauChoisi = X_BORD_VERTICAL_HAUT_DROITE_PRES_D_UN_COIN;
					yMorceauChoisi = Y_BORD_VERTICAL_HAUT_DROITE_PRES_D_UN_COIN;
				}
			}
		} else {
			if (connexionDroite) {
				if (connexionGauche) {
					xMorceauChoisi = X_BORD_HORIZONTAL_HAUT_DROITE_LOIN_D_UN_COIN;
					yMorceauChoisi = Y_BORD_HORIZONTAL_HAUT_DROITE_LOIN_D_UN_COIN;
				} else {
					xMorceauChoisi = X_BORD_HORIZONTAL_HAUT_DROITE_PRES_D_UN_COIN;
					yMorceauChoisi = Y_BORD_HORIZONTAL_HAUT_DROITE_PRES_D_UN_COIN;
				}
			} else {
				xMorceauChoisi = X_COIN_SORTANT_HAUT_DROITE;
				yMorceauChoisi = Y_COIN_SORTANT_HAUT_DROITE;
			}
		}
		return new int[] { xMorceauChoisi, yMorceauChoisi };
	}

	/**
	 * Choisir l'apparence du quart bas-gauche du carreau Autotile.
	 * 
	 * @param connexionBas
	 *            le carreau Autotile est-il connect� � un voisin ?
	 * @param connexionGauche
	 *            le carreau Autotile est-il connect� � un voisin ?
	 * @param connexionBasGauche
	 *            le carreau Autotile est-il connect� � un voisin ?
	 * @param connexionDroite
	 *            le carreau Autotile est-il connect� � un voisin ?
	 * @param connexionHaut
	 *            le carreau Autotile est-il connect� � un voisin ?
	 * @return coordonn�es (en quarts de carreaux) du morceau d'Autotile �
	 *         peindre sur le carreau
	 */
	private int[] choisirLeQuartBasGaucheDuCarreau(final boolean connexionBas, final boolean connexionGauche,
			final boolean connexionBasGauche, final boolean connexionDroite, final boolean connexionHaut) {
		final int xMorceauChoisi;
		final int yMorceauChoisi;
		if (connexionBas) {
			if (connexionGauche) {
				if (connexionBasGauche) {
					if (connexionDroite) {
						if (connexionHaut) {
							xMorceauChoisi = X_PLEIN_BAS_GAUCHE;
							yMorceauChoisi = Y_PLEIN_BAS_GAUCHE;
						} else {
							xMorceauChoisi = X_PLEIN_BAS_GAUCHE_VIDE_EN_HAUT;
							yMorceauChoisi = Y_PLEIN_BAS_GAUCHE_VIDE_EN_HAUT;
						}
					} else {
						if (connexionHaut) {
							xMorceauChoisi = X_PLEIN_BAS_GAUCHE_VIDE_A_DROITE;
							yMorceauChoisi = Y_PLEIN_BAS_GAUCHE_VIDE_A_DROITE;
						} else {
							xMorceauChoisi = X_PLEIN_BAS_GAUCHE_VIDE_EN_HAUT_ET_A_DROITE;
							yMorceauChoisi = Y_PLEIN_BAS_GAUCHE_VIDE_EN_HAUT_ET_A_DROITE;
						}
					}
				} else {
					xMorceauChoisi = X_COIN_RENTRANT_BAS_GAUCHE;
					yMorceauChoisi = Y_COIN_RENTRANT_BAS_GAUCHE;
				}
			} else {
				if (connexionHaut) {
					xMorceauChoisi = X_BORD_VERTICAL_BAS_GAUCHE_LOIN_D_UN_COIN;
					yMorceauChoisi = Y_BORD_VERTICAL_BAS_GAUCHE_LOIN_D_UN_COIN;
				} else {
					xMorceauChoisi = X_BORD_VERTICAL_BAS_GAUCHE_PRES_D_UN_COIN;
					yMorceauChoisi = Y_BORD_VERTICAL_BAS_GAUCHE_PRES_D_UN_COIN;
				}
			}
		} else {
			if (connexionGauche) {
				if (connexionDroite) {
					xMorceauChoisi = X_BORD_HORIZONTAL_BAS_GAUCHE_LOIN_D_UN_COIN;
					yMorceauChoisi = Y_BORD_HORIZONTAL_BAS_GAUCHE_LOIN_D_UN_COIN;
				} else {
					xMorceauChoisi = X_BORD_HORIZONTAL_BAS_GAUCHE_PRES_D_UN_COIN;
					yMorceauChoisi = Y_BORD_HORIZONTAL_BAS_GAUCHE_PRES_D_UN_COIN;
				}
			} else {
				xMorceauChoisi = X_COIN_SORTANT_BAS_GAUCHE;
				yMorceauChoisi = Y_COIN_SORTANT_BAS_GAUCHE;
			}
		}
		return new int[] { xMorceauChoisi, yMorceauChoisi };
	}

	/**
	 * Choisir l'apparence du quart bas-droite du carreau Autotile.
	 * 
	 * @param connexionBas
	 *            le carreau Autotile est-il connect� � un voisin ?
	 * @param connexionDroite
	 *            le carreau Autotile est-il connect� � un voisin ?
	 * @param connexionBasDroite
	 *            le carreau Autotile est-il connect� � un voisin ?
	 * @param connexionGauche
	 *            le carreau Autotile est-il connect� � un voisin ?
	 * @param connexionHaut
	 *            le carreau Autotile est-il connect� � un voisin ?
	 * @return coordonn�es (en quarts de carreaux) du morceau d'Autotile �
	 *         peindre sur le carreau
	 */
	private int[] choisirLeQuartBasDroiteDuCarreau(final boolean connexionBas, final boolean connexionDroite,
			final boolean connexionBasDroite, final boolean connexionGauche, final boolean connexionHaut) {
		final int xMorceauChoisi;
		final int yMorceauChoisi;
		if (connexionBas) {
			if (connexionDroite) {
				if (connexionBasDroite) {
					if (connexionGauche) {
						if (connexionHaut) {
							xMorceauChoisi = X_PLEIN_BAS_DROITE;
							yMorceauChoisi = Y_PLEIN_BAS_DROITE;
						} else {
							xMorceauChoisi = X_PLEIN_BAS_DROITE_VIDE_EN_HAUT;
							yMorceauChoisi = Y_PLEIN_BAS_DROITE_VIDE_EN_HAUT;
						}
					} else {
						if (connexionHaut) {
							xMorceauChoisi = X_PLEIN_BAS_DROITE_VIDE_A_GAUCHE;
							yMorceauChoisi = Y_PLEIN_BAS_DROITE_VIDE_A_GAUCHE;
						} else {
							xMorceauChoisi = X_PLEIN_BAS_DROITE_VIDE_EN_HAUT_ET_A_GAUCHE;
							yMorceauChoisi = Y_PLEIN_BAS_DROITE_VIDE_EN_HAUT_ET_A_GAUCHE;
						}
					}
				} else {
					xMorceauChoisi = X_COIN_RENTRANT_BAS_DROITE;
					yMorceauChoisi = Y_COIN_RENTRANT_BAS_DROITE;
				}
			} else {
				if (connexionHaut) {
					xMorceauChoisi = X_BORD_VERTICAL_BAS_DROITE_LOIN_D_UN_COIN;
					yMorceauChoisi = Y_BORD_VERTICAL_BAS_DROITE_LOIN_D_UN_COIN;
				} else {
					xMorceauChoisi = X_BORD_VERTICAL_BAS_DROITE_PRES_D_UN_COIN;
					yMorceauChoisi = Y_BORD_VERTICAL_BAS_DROITE_PRES_D_UN_COIN;
				}

			}
		} else {
			if (connexionDroite) {
				if (connexionGauche) {
					xMorceauChoisi = X_BORD_HORIZONTAL_BAS_DROITE_LOIN_D_UN_COIN;
					yMorceauChoisi = Y_BORD_HORIZONTAL_BAS_DROITE_LOIN_D_UN_COIN;
				} else {
					xMorceauChoisi = X_BORD_HORIZONTAL_BAS_DROITE_PRES_D_UN_COIN;
					yMorceauChoisi = Y_BORD_HORIZONTAL_BAS_DROITE_PRES_D_UN_COIN;
				}
			} else {
				xMorceauChoisi = X_COIN_SORTANT_BAS_DROITE;
				yMorceauChoisi = Y_COIN_SORTANT_BAS_DROITE;
			}
		}
		return new int[] { xMorceauChoisi, yMorceauChoisi };
	}

	public void render(SpriteBatch batch, int x, int y, int w, int h, int n, int[][] l) {

		if (Map.needRefresh) {
			textures[x][y] = calculerAutotile(x, y, w, h, n, l);
		}

		if (textures != null) {
			if (this.anime) {
				int i = ScreenEditor.animation % 4;
				if (i < textures[x][y].length)
					if (textures[x][y][i] != null) {
						batch.draw(textures[x][y][i], x * 16 + 16, y * 16 + 16 + 5);
					}
			} else {
				int i = 0;
				if (i < textures[x][y].length)
					if (textures[x][y][i] != null) {
						batch.draw(textures[x][y][i], x * 16 + 16, y * 16 + 16 + 5);
					}
			}
		}

	}

	public void render(SpriteBatch batch, int i, int j) {
		Pixmap p = new Pixmap(TAILLE_D_UN_CARREAU, TAILLE_D_UN_CARREAU, Pixmap.Format.RGBA8888);
		// final Graphics2D g2d = (Graphics2D) resultat.createGraphics();

		// Pixmap p2 = new Pixmap(texture);

		p.drawPixmap(pix, 0, 0, 0, 0, TAILLE_MORCEAU * 4, TAILLE_MORCEAU * 4);

		if (texTest != null) {
			texTest.dispose();
		}

		texTest = new Texture(p);

		batch.draw(texTest, i, j);

		p.dispose();
		// t.dispose();

	}

	public void dispose() {
		if (texTest != null) {
			texTest.dispose();
		}

		for (int i = 0; i < textures.length; ++i) {
			for (int j = 0; j < textures[i].length; ++j) {
				for (int k = 0; k < textures[i][j].length; ++k) {
					textures[i][j][k].dispose();
				}
			}
		}
	}

}