package com.izabelsilva.bdsqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    EditText editNome, editContato, editTipo;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNome = (EditText) findViewById(R.id.editNome);
        editContato = (EditText) findViewById(R.id.editContato);
        editTipo = (EditText) findViewById(R.id.editTipo);

        db = openOrCreateDatabase("ContatosDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS contatos (Nome VARCHAR, Contato VARCHAR, Tipo VARCHAR);");

    }

    public void btAdicionar(View view){

        if(editNome.getText().toString().trim().length() == 0 || editContato.getText().toString().trim().length() == 0
                || editTipo.getText().toString().trim().length() == 0) {
            showMessage("ERRO", "Preencha corretamente os dados!");
            return;

            db.execSQL("INSERT INTO contatos VALUES('" + editNome.getText() + "','" + editContato.getText() + "','" + editTipo.getText() + "');");
            showMessage("OK", "Dados Gravados");
            clear();
        }
    }

    public void btDeletar(View view){

        if(editNome.getText().toString().trim().length() == 0){
            showMessage("ERRO", "Preencha corretamente os dados!");
            return;
        }

        Cursor c = db.rawQuery("SELECT * FROM contatos WHERE Nome = '"+editNome.getText()+"'", null);
        if (c.moveToFirst()){
            db.execSQL("DELETE * FROM contatos WHERE Nome = '"+editNome.getText()+"'");
                showMessage("SUCESSO","DADOS DELETADOS");
        }
        else {
            showMessage("ERRO","INVÁLIDO");
        }

        clearText();
    }

    public void btAtualizar(View view) {

        if (editNome.getText().toString().trim().length() == 0) {
            showMessage("ERRO", "Preencha corretamente os dados!");
            return;
        }

        Cursor c = db.rawQuery("SELECT * FROM contatos WHERE Nome = '"+editNome.getText()+"'", null);
        if (c.moveToFirst()){
            db.execSQL("UPDATE contatos SET Nome = '"+editNome.getText()+ "', Contatos ='" + editContato.getText()+
                    "', Tipo = '" + editTipo.getText() + "'WHERE Nome = '" + editNome.getText() + "'" );
            showMessage("SUCESSO","DADOS DELETADOS");
        }
        else {
            showMessage("ERRO","INVÁLIDO");
        }

        clearText();
    }

    public void btBuscarContato(View view){
        if(editNome.getText().toString().trim().length()==0){
            showMessage("Erro!" , "Informe um nome");
            return;

        }

        Cursor cursor = db.rawQuery("SELECT * FROM contatos WHERE Nome='"+editNome.getText()+"'", null);
        if (cursor.moveToFirst()){
            editNome.setText(cursor.getString(0));
            editContato.setText(cursor.getString(1));
            editTipo.setText(cursor.getString(2));
        }
        else{
            showMessage("Erro!"," Faça uma busca primeiro");
            clearText();
        }

    }

    public void btListarContatos(View view){
        Cursor cursor = db.rawQuery("SELECT * FROM contatos", null);
        if(cursor.getCount()==0){
            showMessage("Erro!","Não foram encontrados resultados para sua busca!");
            return;
        }
        StringBuffer buffer=new StringBuffer();
        while(cursor.moveToNext()){
            buffer.append("Nome" + cursor.getString(0) +"\n");
        }
        showMessage("Contato: ", buffer.toString());
    }


    public  void showMessage(String title,String message ){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }

    public void clearText(){
        editNome.setText("");
        editContato.setText("");
        editTipo.setText("");
    }



}
