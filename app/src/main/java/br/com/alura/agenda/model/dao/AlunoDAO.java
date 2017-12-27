package br.com.alura.agenda.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.alura.agenda.model.Aluno;

/**
 * Created by Gabriel on 13/10/2017.
 */

public class AlunoDAO extends SQLiteOpenHelper {
    public AlunoDAO(Context context) {
        super(context, "Agenda", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Alunos (id INTEGER PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "endereco TEXT, " +
                "telefone TEXT, " +
                "site TEXT, " +
                "nota REAL, " +
                "caminhoFoto TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int versaoNova) {
        String sql = "";
        switch (versaoAntiga) {
            case 1:
                sql = "ALTER TABLE Alunos ADD COLUMN caminhoFoto TEXT";
                db.execSQL(sql); // indo para versao 2

//            case 2:
//                sql = "ALTER TABLE Alunos ADD COLUMN cpf TEXT";
//                db.execSQL(sql); // indo para versao 3
//                break;

        }
    }

    public void insere(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase(); //referencia pro banco (escrever dados)
        ContentValues dados = pegaDadosDoAluno(aluno);
        //Inserção no BD
        db.insert("Alunos", null, dados);
    }

    public List<Aluno> buscaAlunos() {
        SQLiteDatabase db = getReadableDatabase(); //referencia pro banco (ler dados)
        Cursor c = db.rawQuery("SELECT * FROM Alunos;", null); //rawQuery devolve resultado da busca em forma de cursor
        List<Aluno> alunos = new ArrayList<Aluno>();

        //percore o cursos e vai preenchendo a lista de alunos
        while (c.moveToNext()){
            Aluno aluno = new Aluno();
            aluno.setId(c.getLong(c.getColumnIndex("id")));
            aluno.setNome(c.getString(c.getColumnIndex("nome")));
            aluno.setEndereco(c.getString(c.getColumnIndex("endereco")));
            aluno.setTelefone(c.getString(c.getColumnIndex("telefone")));
            aluno.setSite(c.getString(c.getColumnIndex("site")));
            aluno.setNota(c.getDouble(c.getColumnIndex("nota")));
            aluno.setCaminhoDaFoto(c.getString(c.getColumnIndex("caminhoFoto")));
            alunos.add(aluno);
        }
        c.close();
        return alunos;
    }

    public void deletar(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {aluno.getId().toString()};
        db.delete("Alunos", "id = ?", params);
    }

    public void altera(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = pegaDadosDoAluno(aluno);

        String[] params = {aluno.getId().toString()};
        db.update("Alunos", dados, "id = ?", params);
    }

    private ContentValues pegaDadosDoAluno(Aluno aluno) {
        //Preparando os dados no modelo do banco, tbm evita SQLInjection
        ContentValues dados = new ContentValues();
        dados.put("nome", aluno.getNome());
        dados.put("endereco", aluno.getEndereco());
        dados.put("telefone", aluno.getTelefone());
        dados.put("site", aluno.getSite());
        dados.put("nota", aluno.getNota());
        dados.put("caminhoFoto", aluno.getCaminhoDaFoto());

        return dados;

    }
}
