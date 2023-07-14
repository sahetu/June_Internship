package june.internship;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment {

    Button profile, changePassword, logout, myOrder, customerSupport, termsCondition, paymentPolicy;
    SharedPreferences sp;

    String[] appPermission = {Manifest.permission.CALL_PHONE};
    private static final int PERMISSION_REQUEST_CODE = 1240;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        sp = getActivity().getSharedPreferences(ConstantData.PREF, Context.MODE_PRIVATE);

        profile = view.findViewById(R.id.profile_button);
        myOrder = view.findViewById(R.id.profile_my_order);
        changePassword = view.findViewById(R.id.profile_change_password);
        logout = view.findViewById(R.id.profile_logout);

        customerSupport = view.findViewById(R.id.profile_customer_support);
        termsCondition = view.findViewById(R.id.profile_terms_condition);
        paymentPolicy = view.findViewById(R.id.profile_payment_policy);

        paymentPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),WebviewActivity.class);
                getActivity().startActivity(intent);
            }
        });

        termsCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://brainybeaminfotech.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        customerSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermission()) {
                    dialCall();
                }
            }
        });

        myOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyOrderActivity.class);
                startActivity(intent);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sp.edit().remove(ConstantData.USERID).commit();
                sp.edit().clear().commit();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void dialCall() {
        //Intent intent = new Intent(Intent.ACTION_DIAL);
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:+917433050707"));
        startActivity(intent);
    }

    public boolean checkAndRequestPermission() {
        List<String> listPermission = new ArrayList<>();
        for (String perm : appPermission) {
            if (ContextCompat.checkSelfPermission(getActivity(), perm) != PackageManager.PERMISSION_GRANTED) {
                listPermission.add(perm);
            }
        }
        if (!listPermission.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermission.toArray(new String[listPermission.size()]), PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            HashMap<String, Integer> permissionResult = new HashMap<>();
            int deniedCount = 0;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResult.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }
            if (deniedCount == 0) {
                dialCall();
            } else {
                for (Map.Entry<String, Integer> entry : permissionResult.entrySet()) {
                    String permName = entry.getKey();
                    int permResult = entry.getValue();
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permName)) {
                        /*showDialogPermission("", "This App needs Read External Storage And Location permissions to work whithout and problems.",*/
                        showDialogPermission("", "This App needs Call Phone permissions to work whithout and problems.",
                                "Yes, Grant permissions", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        checkAndRequestPermission();
                                    }
                                },
                                "No, Exit app", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        getActivity().finishAffinity();
                                    }
                                }, false);
                    } else {
                        showDialogPermission("", "You have denied some permissions. Allow all permissions at [Setting] > [Permissions]",
                                "Go to Settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts("package", getActivity().getPackageName(), null));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                }, "No, Exit app", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        getActivity().finish();
                                    }
                                }, false);
                        break;
                    }
                }
            }
        }
    }

    public AlertDialog showDialogPermission(String title, String msg, String positiveLable, DialogInterface.OnClickListener positiveOnClickListener, String negativeLable, DialogInterface.OnClickListener negativeOnClickListener, boolean isCancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setCancelable(isCancelable);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveLable, positiveOnClickListener);
        builder.setNegativeButton(negativeLable, negativeOnClickListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

}