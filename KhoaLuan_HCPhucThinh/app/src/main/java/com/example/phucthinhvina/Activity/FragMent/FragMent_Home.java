package com.example.phucthinhvina.Activity.FragMent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.phucthinhvina.Activity.Bill.CartActivity;
import com.example.phucthinhvina.Activity.SearchActivity;
import com.example.phucthinhvina.Adapter.CategoryAdapter;
import com.example.phucthinhvina.Model.CategoryModels;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.phucthinhvina.Adapter.BannerAdapter;
import com.example.phucthinhvina.Adapter.SanPhamAdapter;
import com.example.phucthinhvina.Model.SanPhamModels;
import com.example.phucthinhvina.Presenter.SanPhamPreSenter;
import com.example.phucthinhvina.Presenter.SanPhamView;
import com.example.phucthinhvina.R;

import java.util.ArrayList;
import java.util.List;

public class FragMent_Home  extends Fragment implements SanPhamView {
    View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<String> arrayList;
    private ViewPager viewPager;
    private FirebaseFirestore db;
    private BannerAdapter bannerAdapter;
    private SanPhamPreSenter sanPhamPreSenter;
    private  ArrayList<SanPhamModels> arr_sp,arr_sp_nb,arr_sp_tu,arr_sp_hq,arr_sp_mc,arr_sp_yt,arr_sp_lau,arr_sp_gy;
    private SanPhamAdapter sanPhamAdapter,sanPhamNBAdapter,sanPhamTUAdapter,sanPhamHQAdapter,sanPhamMCAdapter,sanPhamYTAdapter,sanPhamLauAdapter,sanPhamGYAdapter;
    private RecyclerView rcvCategory,rcvSP,rcvSpNoiBat,rcvSPThucUong,rcvSPHQ,rcvSPMC,rcvSPYT,rcvSPLau,rcvSPGY;
    private ImageButton imgBtnDanhMuc;


    //Category

    List<CategoryModels> categoryList;
    CategoryAdapter categoryAdapter;
    FragMent_HomeListener activityCallback;
    public interface FragMent_HomeListener {
        void onButtonClick();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            activityCallback = (FragMent_HomeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " You must implement FirstFragmentListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);

        InitWidget();
        Init();
        InitSanPham();

//        imgBtnDanhMuc.setOnClickListener(view ->{
//            activityCallback.onButtonClick();
//        });

        return  view;
    }

    private void InitSanPham() {
        arr_sp = new ArrayList<>();
        arr_sp_nb = new ArrayList<>();
        arr_sp_tu = new ArrayList<>();
        arr_sp_hq = new ArrayList<>();
        arr_sp_mc = new ArrayList<>();
        arr_sp_yt = new ArrayList<>();
        arr_sp_lau = new ArrayList<>();
        arr_sp_gy = new ArrayList<>();
        sanPhamPreSenter = new SanPhamPreSenter(this);
        sanPhamPreSenter.HandlegetDataSanPham();
        sanPhamPreSenter.HandlegetDataSanPhamNB();
        sanPhamPreSenter.HandlegetDataSanPhamTU();
        sanPhamPreSenter.HandlegetDataSanPhamHQ();
        sanPhamPreSenter.HandlegetDataSanPhamMC();
        sanPhamPreSenter.HandlegetDataSanPhamYT();
        sanPhamPreSenter.HandlegetDataSanPhamLau();
        sanPhamPreSenter.HandlegetDataSanPhamGY();
    }
///Tạo banner
    private void Init() {
        arrayList = new ArrayList<>();
        rcvCategory = view.findViewById(R.id.rcvCategory);
        db= FirebaseFirestore.getInstance();
        db.collection("Banner").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                 for(QueryDocumentSnapshot d : queryDocumentSnapshots){
                     arrayList.add(d.getString("hinhanh"));
                 }
                 bannerAdapter = new BannerAdapter(getContext(),arrayList);
                 viewPager.setAdapter(bannerAdapter);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    //3s sang 1 banner khác
                    public void run() {
                        int k=viewPager.getCurrentItem();
                        if(k>=arrayList.size()-1){
                             k  = 0;
                        }else{
                            k++;
                        }
                        handler.postDelayed(this,3000);
                        viewPager.setCurrentItem(k,true);

                    }
                },3000);

            }
        });
        //Top
        view.findViewById(R.id.home_search).setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), SearchActivity.class));
        });

        view.findViewById(R.id.shopcart).setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), CartActivity.class));
        });


        //Category
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rcvCategory.setLayoutManager(layoutManager);
//        rcvCategory.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL
//                , false));
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        rcvCategory.setAdapter(categoryAdapter);

        db.collection("LoaiSP")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                CategoryModels categoryModels = document.toObject(CategoryModels.class);
                                categoryList.add(categoryModels);
                                categoryAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Toast.makeText(getActivity(), "Error"+task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    private void InitWidget() {
        viewPager = view.findViewById(R.id.viewpager);

        rcvCategory = view.findViewById(R.id.rcvCategory);
        rcvSP = view.findViewById(R.id.rcvSP);
        rcvSpNoiBat = view.findViewById(R.id.rcvNB);
        rcvSPThucUong = view.findViewById(R.id.rcvTU);
        rcvSPHQ = view.findViewById(R.id.rcvHQ);
        rcvSPMC = view.findViewById(R.id.rcvMC);
        rcvSPYT = view.findViewById(R.id.rcvYT);
        rcvSPLau = view.findViewById(R.id.rcvLau);
//        rcvSPGY = view.findViewById(R.id.rcvGY);
        //imgBtnDanhMuc = view .findViewById(R.id.home_danhmuc);
    }

    @Override
    public void getDataSanPham(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong,
                               String nhasanxuat, Long type,String trongluong) {
        arr_sp.add(new SanPhamModels(id,tensp,giatien,hinhanh,loaisp,mota,soluong,nhasanxuat,type,trongluong));
        sanPhamAdapter = new SanPhamAdapter(getContext(),arr_sp);
        rcvSP.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        rcvSP.setAdapter(sanPhamAdapter);

    }

    @Override
    public void OnEmptyList() {

    }

    @Override
    public void getDataSanPhamNB(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String nhasanxuat, Long type, String trongluong) {
        arr_sp_nb.add(new SanPhamModels(id,tensp,giatien,hinhanh,loaisp,mota,soluong,nhasanxuat,type,trongluong));
        sanPhamNBAdapter = new SanPhamAdapter(getContext(),arr_sp_nb,2);
        rcvSpNoiBat.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        rcvSpNoiBat.setAdapter(sanPhamNBAdapter);
    }
    @Override
    public void getDataSanPhamTU(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String nhasanxuat, Long type, String trongluong) {
        arr_sp_tu.add(new SanPhamModels(id,tensp,giatien,hinhanh,loaisp,mota,soluong,nhasanxuat,type,trongluong));
        sanPhamTUAdapter = new SanPhamAdapter(getContext(),arr_sp_tu,3);
        rcvSPThucUong.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        rcvSPThucUong.setAdapter(sanPhamTUAdapter);
    }
    @Override
    public void getDataSanPhamHQ(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String nhasanxuat, Long type, String trongluong) {
        arr_sp_hq.add(new SanPhamModels(id,tensp,giatien,hinhanh,loaisp,mota,soluong,nhasanxuat,type,trongluong));
        sanPhamHQAdapter = new SanPhamAdapter(getContext(),arr_sp_hq,4);
        rcvSPHQ.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        rcvSPHQ.setAdapter(sanPhamHQAdapter);
    }
    @Override
    public void getDataSanPhamMC(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String nhasanxuat, Long type, String trongluong) {
        arr_sp_mc.add(new SanPhamModels(id,tensp,giatien,hinhanh,loaisp,mota,soluong,nhasanxuat,type,trongluong));
        sanPhamMCAdapter = new SanPhamAdapter(getContext(),arr_sp_mc,5);
        rcvSPMC.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        rcvSPMC.setAdapter(sanPhamMCAdapter);
    }
    @Override
    public void getDataSanPhamYT(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String nhasanxuat, Long type, String trongluong) {
        arr_sp_yt.add(new SanPhamModels(id,tensp,giatien,hinhanh,loaisp,mota,soluong,nhasanxuat,type,trongluong));
        sanPhamYTAdapter = new SanPhamAdapter(getContext(),arr_sp_yt,6);
        rcvSPYT.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        rcvSPYT.setAdapter(sanPhamYTAdapter);
    }
    @Override
    public void getDataSanPhamLau(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String nhasanxuat, Long type, String trongluong) {
        arr_sp_lau.add(new SanPhamModels(id,tensp,giatien,hinhanh,loaisp,mota,soluong,nhasanxuat,type,trongluong));
        sanPhamLauAdapter = new SanPhamAdapter(getContext(),arr_sp_lau,7);
        rcvSPLau.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        rcvSPLau.setAdapter(sanPhamLauAdapter);
    }
    @Override
    public void getDataSanPhamGY(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String nhasanxuat, Long type, String trongluong) {
        arr_sp_gy.add(new SanPhamModels(id,tensp,giatien,hinhanh,loaisp,mota,soluong,nhasanxuat,type,trongluong));
        sanPhamGYAdapter = new SanPhamAdapter(getContext(),arr_sp_gy,8);
        rcvSPGY.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        rcvSPGY.setAdapter(sanPhamGYAdapter);
    }
}
