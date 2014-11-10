package com.azim;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MyApp extends Activity implements OnClickListener
{
	EditText editRollno,editName,editMarks;
	Button btnAdd,btnDelete,btnModify,btnView,btnViewAll;
	SQLiteDatabase db;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        editRollno=(EditText)findViewById(R.id.editRollno);
        editName=(EditText)findViewById(R.id.editName);
        editMarks=(EditText)findViewById(R.id.editMarks);
        btnAdd=(Button)findViewById(R.id.btnAdd);
        btnDelete=(Button)findViewById(R.id.btnDelete);
        btnModify=(Button)findViewById(R.id.btnModify);
        btnView=(Button)findViewById(R.id.btnView);
        btnViewAll=(Button)findViewById(R.id.btnViewAll);
        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnModify.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnViewAll.setOnClickListener(this);
        db=openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
		db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR,name VARCHAR,marks VARCHAR);");
    }
    
    public void onClick(View view)
    {
    	if(view==btnAdd)
    	{
    		this.insert();
    	}else if(view==btnDelete)
    	{
    		this.delete();
    	}else if(view==btnModify)
    	{
			this.modify();
    	}else if(view==btnView)
    	{
    		this.view();
    	}else if(view==btnViewAll)
    	{
    		this.viewAll();
    	}
    }
    private void showMessage(String title,String message)
    {
    	Builder builder=new Builder(this);
    	builder.setCancelable(true);
    	builder.setTitle(title);
    	builder.setMessage(message);
    	builder.show();
	}
    private void clearText()
    {
    	editRollno.setText("");
    	editName.setText("");
    	editMarks.setText("");
    	editRollno.requestFocus();
    }
    
    private void insert()
    {
		if(editRollno.getText().toString().trim().length()==0||
	 		   editName.getText().toString().trim().length()==0||
	 		   editMarks.getText().toString().trim().length()==0
	   	)
		{
 			showMessage("Error", "Introduzca los valores");
 			return;
 		}
 		db.execSQL(
			"INSERT INTO student VALUES('"+
			editRollno.getText()+"','"+
			editName.getText()+"','"+
			editMarks.getText()+"');"
		);
 		showMessage("Exito", "Registro agregado");
 		clearText();
    }
    
    private void delete()
    {
    	if(editRollno.getText().toString().trim().length()==0)
		{
			showMessage("Error", "Por favor introduzca el codigo");
			return;
		}
		Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+editRollno.getText()+"'", null);
		if(c.moveToFirst())
		{
			db.execSQL("DELETE FROM student WHERE rollno='"+editRollno.getText()+"'");
			showMessage("Exito", "Registro eliminado");
		}
		else
		{
			showMessage("Error", "Código invalido");
		}
		clearText();
    }
    
    private void modify()
    {
		if(editRollno.getText().toString().trim().length()==0)
		{
			showMessage("Error", "Introduzca el código");
			return;
		}
		Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+editRollno.getText()+"'", null);
		if(c.moveToFirst())
		{
			db.execSQL(
				"UPDATE student SET name='"+editName.getText()+"',"+
				"marks='"+editMarks.getText()+
				"' WHERE "+
				"rollno='"+editRollno.getText()+"'"
			);
			showMessage("Exito", "Registro modificado");
		}
		else
		{
			showMessage("Error", "Código invalido");
		}
		clearText();
    }
    
    private void view()
    {
		if(editRollno.getText().toString().trim().length()==0)
		{
			showMessage("Error", "Introduzca el código");
			return;
		}
		Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+editRollno.getText()+"'", null);
		if(c.moveToFirst())
		{
			editName.setText(c.getString(1));
			editMarks.setText(c.getString(2));
		}
		else
		{
			showMessage("Error", "Código invalido");
			clearText();
		}
    }
    
    private void viewAll()
    {
		Cursor c=db.rawQuery("SELECT * FROM student", null);
		if(c.getCount()==0)
		{
			showMessage("Error", "Sin resultados");
			return;
		}
		StringBuffer buffer=new StringBuffer();
		while(c.moveToNext())
		{
			buffer.append("Código: "+c.getString(0)+"\n");
			buffer.append("Nombre: "+c.getString(1)+"\n");
			buffer.append("Calificación: "+c.getString(2)+"\n\n");
		}
		showMessage("Estudiantes", buffer.toString());
    }

}