//----------------------------------------------------------------------------------
// Name:		scenario
// Author:		Seyed Hami Nourbakhsh (SHN)
// Company:		-
// Date:		26.Sept.2011
// Application  description:	
//				This class specify a 3D scenario and provide methods to initialize 
//				it from resource files and draw it   
//
// To do:		- How to hand over Buffers to the function, reduce number of func
//										
//										
// Modification history:	Who	Date		What
//							SHN	26.09.11	Initial version. First draft
//							SHN	xx.10.11	
//----------------------------------------------------------------------------------

package com.ht3dd_eb;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.microedition.khronos.opengles.GL10;

class scenario{    	
	
	//private int 		OBJ_MAX = 2;
	private IntBuffer   mVertexBuffer_0;   
	private IntBuffer   mVertexBuffer_1;   
	private IntBuffer   mVertexBuffer_2;   
	private IntBuffer   mColorBuffer;    
	private ByteBuffer  mIndexBuffer_0;
	private ByteBuffer  mIndexBuffer_1;
	private ByteBuffer  mIndexBuffer_2;
	private FloatBuffer	mtextureBuffer;
	
	//private grObj [] 		mgrObj;
	
	public scenario()   
	{        
		/**/
		int one = 0x10000;        
		int vertices_0[] = {                
				-one, -one, -one,             
				one, -one, -one,                
				one,  one, -one,                
				-one,  one, -one,               
				-one, -one,  one,               
				one, -one,  one,                
				one,  one,  one,                
				-one,  one,  one,        
				};        
		
		//int [] vertices_1 = new int[25];
		
		int vertices_1[] = {                
				-one, -one, -one>>2,             
				one, -one, -one>>2,                
				one,  one, -one>>2,                
				-one,  one, -one>>2,               
				-one, -one,  one>>2,               
				one, -one,  one>>2,                
				one,  one,  one>>2,                
				-one,  one,  one>>2,        
				};        
		
		int vertices_2[] = {  			
				-one*8, -one*5, -one,
				+one*8, -one*5, -one,
				+one*8,  one*5, -one,
				-one*8,  one*5, -one,                       
				};        
				
		int colors[] = {             
				0,    0,    0,  one,   
				one,    0,    0,  one,    
				one,  one,    0,  one,           
				0,  one,    0,  one,             
				0,    0,  one,  one,             
				one,    0,  one,  one,           
				one,  one,  one,  one,           
				0,  one,  one,  one,        
				};   
		
		
		byte indices_0[] = {       
				0, 4, 5,    0, 5, 1,    
				1, 5, 6,    1, 6, 2,     
				2, 6, 7,    2, 7, 3,     
				3, 7, 4,    3, 4, 0,     
				4, 7, 6,    4, 6, 5,     
				3, 0, 1,    3, 1, 2,      
				};     
		
		byte indices_2[] = {       
				0, 1, 1, 2, 2, 3, 3, 0,             
				};        
		
		 float texCoords_0[] = {         
				 1.0f, 1.0f,     0.0f, 1.0f,         
				 0.0f, 0.0f,     1.0f, 0.0f, 
				 
				 1.0f, 1.0f,     0.0f, 1.0f,         
				 0.0f, 0.0f,     1.0f, 0.0f, 
				 
				 1.0f, 1.0f,     0.0f, 1.0f,         
				 0.0f, 0.0f,     1.0f, 0.0f,
				 
				 1.0f, 1.0f,     0.0f, 1.0f,         
				 0.0f, 0.0f,     1.0f, 0.0f, 
				 
				 1.0f, 1.0f,     0.0f, 1.0f,         
				 0.0f, 0.0f,     1.0f, 0.0f, 
				 
				 1.0f, 1.0f,     0.0f, 1.0f,       
				 0.0f, 0.0f,     1.0f, 0.0f,    
				 };
				 
		//for (int c = 0; c < OBJ_MAX; c++)
			//mgrObj[c] = new grObj(); 
				
		setVert_0	( vertices_1 );
		setVert_2	( vertices_2 );
		
		setColor	( colors );
		setText		( texCoords_0 );
		
		setInd_0	( indices_0 );
		setInd_2	( indices_2 );
		
	} //constructor    
	
	public void draw(GL10 gl) {
		
		objDraw(gl);
		gridDraw(gl);
		 
	}    //draw
	
	protected void objDraw(GL10 gl){
		gl.glFrontFace(GL10.GL_CW);    
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);            
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer_0);  
		
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);  
		gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);    
		
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY); 
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mtextureBuffer); 
		
		gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_BYTE, mIndexBuffer_0);  

	}
	
	public void gridDraw(GL10 gl){
		gl.glFrontFace(GL10.GL_CW);    
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);            
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer_2);  
		
		//gl.glEnableClientState(GL10.GL_COLOR_ARRAY);  
		//gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);    
		
		//gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY); 
		//gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mtextureBuffer); 
		
		gl.glDrawElements(GL10.GL_LINES, 8, GL10.GL_UNSIGNED_BYTE, mIndexBuffer_2);  

	}

	protected void setVert_0(int [] vertices){
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4); 
		vbb.order(ByteOrder.nativeOrder());       
		mVertexBuffer_0 = vbb.asIntBuffer();        
		mVertexBuffer_0.put(vertices);       
		mVertexBuffer_0.position(0);      
	}
	
	protected void setVert_2(int [] vertices){
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4); 
		vbb.order(ByteOrder.nativeOrder());       
		mVertexBuffer_2 = vbb.asIntBuffer();        
		mVertexBuffer_2.put(vertices);       
		mVertexBuffer_2.position(0);      
	}

	protected void setColor(int [] colors){
		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);   
		cbb.order(ByteOrder.nativeOrder());      
		mColorBuffer = cbb.asIntBuffer();      
		mColorBuffer.put(colors);      
		mColorBuffer.position(0);      
	}

	protected void setInd_0(byte [] indices){
		mIndexBuffer_0 = ByteBuffer.allocateDirect(indices.length);   
		mIndexBuffer_0.put(indices);     
		mIndexBuffer_0.position(0);    
	}

	protected void setInd_2(byte [] indices){
		mIndexBuffer_2 = ByteBuffer.allocateDirect(indices.length);   
		mIndexBuffer_2.put(indices);     
		mIndexBuffer_2.position(0);    
	}

	protected void setText(float [] texCoords){
		ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length*4);   
		tbb.order(ByteOrder.nativeOrder());    
		mtextureBuffer = tbb.asFloatBuffer();
		mtextureBuffer.put(texCoords);     
		mtextureBuffer.position(0);    				
	}

	
}	// scenario
