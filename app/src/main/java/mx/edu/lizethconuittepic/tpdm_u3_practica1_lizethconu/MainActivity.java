package mx.edu.lizethconuittepic.tpdm_u3_practica1_lizethconu;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText categoria,descripcion,cantidad,tipo;
    Button insertar, eliminar, actualizar, consultar, insertarproducto;
    ListView lista;
    List<Categoria> datosConsulataCategoria;

    DatabaseReference servicioRealtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categoria = findViewById(R.id.categoria);
        descripcion = findViewById(R.id.descripcion);
        cantidad = findViewById(R.id.cantidad);
        tipo = findViewById(R.id.tipo);
        lista = findViewById(R.id.listacategoria);
        insertar = findViewById(R.id.insertar);
        eliminar = findViewById(R.id.eliminar);
        actualizar = findViewById(R.id.actualizar);
        consultar = findViewById(R.id.consultar);
        insertarproducto = findViewById(R.id.insertaproducto);

        servicioRealtime = FirebaseDatabase.getInstance().getReference();

        insertarproducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
                finish();
            }
        });

        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarCategoria();
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elimina();
            }
        });
        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultaTodos();
            }
        });
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualiza();
            }
        });

    }

    private void actualiza(){
        Map<String, Object> map = new HashMap<>();
        Categoria categoria = new Categoria(categoria.getText().toString(), descripcion.getText().toString(), tipo.getText().toString(), cantidad.getText().toString());
        map.put("Categoria", categoria);

        servicioRealtime.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "JALO", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void consultaTodos(){
        servicioRealtime.child("Categorias").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datosConsultaCategoria = new ArrayList<>();

                servicioRealtime.child("Categorias").child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snap : dataSnapshot.getChildren()){
                            Categoria categoria = snap.getValue(Categoria.class);

                            if(categoria!=null){
                                datosConsultaMarcas.add(categoria);
                            }
                        }
                        crearListView();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void crearListView(){
        if(datosConsultaCategoria.size()<=0){
            return;
        }

        String[] nombres = new String[datosConsultaCategoria.size()];
        for(int i = 0; i<nombres.length;i++){
            Categoria j = datosConsultaCategoria.get(i);
            nombres[i] = j.categoria;

        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombres);
        lista.setAdapter(adaptador);

    }

    private void insertar(){
        final Categoria categoria = new Categoria(categoria.getText().toString(), descripcion.getText().toString(), tipo.getText().toString(), cantidad.getText().toString());
        servicioRealtime.child("Categorias").child(gategoria.getText().toString()).push().setValue(categoria)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "JALO", Toast.LENGTH_SHORT).show();
                        categoria.setText("");
                        cantidad.setText("");
                        descripcion.setText("");
                        tipo.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error al insertar!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void eliminar(){
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        final EditText id = new EditText(this);
        id.setHint("Id a eliminar");
        a.setTitle("Eliminar")
                .setMessage("Ingrese el id a eliminar")
                .setView(id).setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eliminarId(id.getText().toString());
            }
        })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarId(String s){
        servicioRealtime.child("Categorias").child(s).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Eliminado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error al eliminar!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
