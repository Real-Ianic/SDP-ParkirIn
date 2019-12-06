package com.example.parkirin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.parkirin.config.Config;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

import static android.app.Activity.RESULT_OK;

public class TopUpFragment extends Fragment {
    Button s,l,sk,topup;
    EditText etNominal;
    TextView txSaldo;
    DatabaseReference reff;
    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration configuration = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(Config.PAYPAL_CLIENT_ID);
    int saldotopup;
    String amount="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_topup,container,false);
    }

    @Override
    public void onDestroy() {
        getContext().stopService(new Intent(getContext(),PayPalService.class));
        super.onDestroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Intent intent = new Intent(getContext(),PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,configuration);
        getContext().startService(intent);
        reff = FirebaseDatabase.getInstance().getReference().child("Member").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                saldotopup = Integer.parseInt(dataSnapshot.child("saldo").getValue().toString());
                txSaldo.setText("Rp "+saldotopup+",00");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menuLogout){
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(getActivity(),LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            getActivity().finish();
        }
        if(item.getItemId()==R.id.menuTrans){
            Intent i = new Intent(this.getActivity(),currTransaksi.class);
            i.putExtra("email",((MainActivity)getActivity()).email);
            startActivity(i);
        }
        return true;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        s=view.findViewById(R.id.btn10k);
        l=view.findViewById(R.id.btn50k);
        sk=view.findViewById(R.id.btn100k);
        topup=view.findViewById(R.id.btnTopUp);
        etNominal=view.findViewById(R.id.etNominal);
        txSaldo=view.findViewById(R.id.txtSaldoTopUp);
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNominal.setText("20000");
            }
        });
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNominal.setText("50000");
            }
        });
        sk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNominal.setText("100000");
            }
        });
        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etNominal.getText().toString())){
                    etNominal.setError( "Jumlah harus diisi!" );
                }else if(Integer.parseInt(etNominal.getText().toString())<20000){
                    etNominal.setError("Minimum Top Up 20000");
                }else{
                    proccesspayment();
                }
            }
        });
    }

    private void proccesspayment() {
        amount = etNominal.getText().toString();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amount),"USD","Pembayaran Top Up",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PAYPAL_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation != null){
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(getActivity(), Topupberhasil.class));
                        reff.child("saldo").setValue(saldotopup+Integer.parseInt(etNominal.getText().toString()));
                        etNominal.setText("");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(resultCode == getActivity().RESULT_CANCELED){
                    Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                }
            }else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
                Toast.makeText(getContext(), "Pembayaran Gagal", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
