package br.com.alura.agenda.view;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.jar.Manifest;

import br.com.alura.agenda.R;
import br.com.alura.agenda.model.dao.AlunoDAO;
import br.com.alura.agenda.model.Aluno;

public class ListaAlunosActivity extends AppCompatActivity {

    private ListView listaAlunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        //Referência para as Views
        Button btNovoAluno = (Button) findViewById(R.id.btNovoAluno);
        listaAlunos = (ListView) findViewById(R.id.lista_alunos);

        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(position);
                Toast.makeText(ListaAlunosActivity.this, "Aluno " + aluno.getNome() + " clicado", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                intent.putExtra("aluno", aluno);
                startActivity(intent);
            }
        });

        btNovoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(intent);
            }
        });

        registerForContextMenu(listaAlunos);

    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        //Recupera posição do aluno clicado
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(info.position);

        MenuItem menuSite = menu.add(R.string.visitar_site);
        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        MenuItem menuSMS = menu.add(R.string.sms);
        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
        MenuItem menuMapa = menu.add(R.string.maps);
        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        final MenuItem menuLigar = menu.add(R.string.ligar);
        MenuItem menuDeletar = menu.add(R.string.deletar);

        //Comportamento menu Visitar Site
        String site = aluno.getSite();
        if(!site.startsWith("http://")){
            site = "http://" + site;
        }
        intentSite.setData(Uri.parse(site));
        menuSite.setIntent(intentSite);

        //Comportamento menu SMS
        intentSMS.setData(Uri.parse("sms:" + aluno.getTelefone()));
        menuSMS.setIntent(intentSMS);

        //Comportamento Mapa
        intentMapa.setData(Uri.parse("geo:0,0?q=" + aluno.getEndereco()));
        menuMapa.setIntent(intentMapa);

        //Comportamento Ligar
        menuLigar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ListaAlunosActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, 123);
                } else {
                    Intent intentLigar = new Intent(Intent.ACTION_CALL);
                    intentLigar.setData(Uri.parse("tel:" + aluno.getTelefone()));
                    menuLigar.setIntent(intentLigar);
                }
                return false;
            }
        });

        //Comportamento Deletar
        menuDeletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(ListaAlunosActivity.this, "Deletando o aluno " + aluno.getNome(), Toast.LENGTH_SHORT).show();
                AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                dao.deletar(aluno);
                dao.close();

                carregaLista();
                return false;
            }
        });
    }

    public void carregaLista() {
        AlunoDAO dao = new AlunoDAO(this);
        List<Aluno> alunos = dao.buscaAlunos();
        dao.close();

        ArrayAdapter<Aluno> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alunos);
        listaAlunos.setAdapter(adapter);
    }
}
