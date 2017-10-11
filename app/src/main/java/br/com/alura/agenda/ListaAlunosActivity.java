package br.com.alura.agenda;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListaAlunosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        //Referência para as Views
        ListView listaAlunos = (ListView) findViewById(R.id.lista_alunos);

        //Mock da lista de alunos
        String[] alunos = {"Rafael", "Gabriel", "Gustavo", "Alan"};

        //criação e associação do adapter que vai converter String para View
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alunos);
        listaAlunos.setAdapter(adapter);


    }
}
