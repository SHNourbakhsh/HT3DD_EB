package com.ht3dd_eb;


/* Memory for Interpolation of received HT values */
public class Mem{
	private static int MEM_SIZE = 5;
	private int c;
	private int[][] mem;

	public boolean m_ok;
	public int[] aV;
	public int[] iP; 
	public int dt2av;
	private int Sx, Sy, Sz;
	private float St, Stt, Stx, Sty, Stz, Sxx, Syy, Szz;
	public float ax, bx, ay, by, az, bz; 
	
	public	Mem()
		{
			c = 0;
			m_ok = false;
			aV  = new int[4];						// average values for x,y,z,t
			iP  = new int[3];						// interpolated values for x,y,z
			mem = new int[MEM_SIZE][4];
			for (int count = 0 ; count < MEM_SIZE; count ++)
			{
				mem[count][0] = 0;					//X
				mem[count][1] = 0;					//Y
				mem[count][2] = 0;					//Z	
				mem[count][3] = 0;					//time
			}
		} // constructor

	public void nV (int v_x, int v_y, int v_z, int t)
		{
			mem[c][0] = v_x;
			mem[c][1] = v_y;
			mem[c][2] = v_z;
			mem[c][3] = t;

			c++;
			if (c == MEM_SIZE)
			{
				c = 0;
				m_ok = true;
			}
			
			if (m_ok)
			{
				calAv();						// calculate average values for x,y,z
				calIp();						// calculate interpolation simple linear regression OLS values for x,y,z
				dt2av = t - aV[3];	
				if (dt2av > 200)
					dt2av = 200;				// limit the time
			}
			else
				dt2av = 200;					// set some value but not zero, since will be used as divisor				
		}

	public int [] gV (int adr)
		{
			int i;
			i = (adr + c) % MEM_SIZE;
			return mem[i];
		}
	
	private void calAv()
	{
		for (int k = 0; k < 4; k++)
		{
			aV[k] = 0;
			for (int i = 0; i < MEM_SIZE; i++)
			{
				aV[k] = aV[k] + mem[i][k];
			}
			aV[k] = aV[k] / MEM_SIZE;
		}
	
	}	
	
	private void calIp()
	{
		
		Sx = 0;
		Sy = 0;
		Sz = 0;
		St = 0;
		Stt = 0;
		Stx = 0;
		Sty = 0;
		Stz = 0;
		Sxx = 0;
		Syy = 0;
		Szz = 0;
		
		for (int i = 0; i < MEM_SIZE; i++)
		{
			Sx = Sx + mem[i][0];
			Sy = Sy + mem[i][1];
			Sz = Sz + mem[i][2];
			St = St + mem[i][3];
			Stt = Stt + mem[i][3]*mem[i][3];
			Stx = Stx + mem[i][3]*mem[i][0];
			Sty = Sty + mem[i][3]*mem[i][1];
			Stz = Stz + mem[i][3]*mem[i][2];
			Sxx = Sxx + mem[i][0]*mem[i][0];
			Syy = Syy + mem[i][1]*mem[i][1];
			Szz = Szz + mem[i][2]*mem[i][2];
			
		}
		bx = (MEM_SIZE * Stx - St * Sx) / (MEM_SIZE * Stt - St*St );
		ax = (Sx / MEM_SIZE) - bx / MEM_SIZE * St;
		
		by = (MEM_SIZE * Sty - St * Sy) / (MEM_SIZE * Stt - St*St );
		ay = (Sy / MEM_SIZE) - by / MEM_SIZE * St;
		
		bz = (MEM_SIZE * Stz - St * Sz) / (MEM_SIZE * Stt - St*St );
		az = (Sz / MEM_SIZE) - bz / MEM_SIZE * St;

	}
}