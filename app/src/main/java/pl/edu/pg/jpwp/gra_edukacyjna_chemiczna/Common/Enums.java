package pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common;

public interface Enums {
    public enum Element{
        H, He,
        Li, Be, B , C , N , O , F , Ne,
        Na, Mg, Al, Si, P , S, Cl, Ar,
        K , Ca, Sc, Ti, V , Cr, Mn, Fe, Co, Ni, Cu, Zn, Ga, Ge, As, Se, Br, Kr,
        Rb, Sr, Y , Zr, Nb, Mo, Tc, Ru, Rh, Pd, Ag, Cd, In, Sn, Sb, Te, I , Xe,
        Cs, Ba, La, Hf, Ta, W , Re, Os, Ir, Pt, Au, Hg, Tl, Pb, Bi, Po, At, Rn,
        Fr, Ra, Ac, Rf, Db, Sg, Bh, Hs, Mt, Ds, Rg, Cn, Nh, Fl, Mc, Lv, Ts, Og,
        Ce, Pr, Nd, Pm, Sm, Eu, Gd, Tb, Dy, Ho, Er, Tm, Yb, Lu,
        Th, Pa, U, Np, Pu, Am, Cm, Bk, Cf, Es, Fm, Md, No, Lr
    }
    public enum Difficulty {
        Easy,
        Medium,
        Hard
    }

    public enum elementType{
        Gas,
        Metal,
        Nonmetal
    }
}
