package com.example.accountbookforme.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.R
import com.example.accountbookforme.adapter.TotalListAdapter
import com.example.accountbookforme.databinding.FragmentListWithMonthBinding
import com.example.accountbookforme.model.Total
import com.example.accountbookforme.util.DateUtil
import com.example.accountbookforme.util.TextUtil
import com.example.accountbookforme.viewmodel.ExpensesViewModel

class StoresFragment : Fragment() {

    private var _binding: FragmentListWithMonthBinding? = null
    private val binding get() = _binding!!

    private val expensesViewModel: ExpensesViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var totalListAdapter: TotalListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListWithMonthBinding.inflate(inflater, container, false)
        val view = binding.root

        // 今月を表示
        binding.month.text = DateUtil.getMonth()

        recyclerView = binding.list
        totalListAdapter = TotalListAdapter()

        // セルのクリック処理
        totalListAdapter.setOnTotalClickListener(
            object : TotalListAdapter.OnTotalClickListener {
                override fun onItemClick(total: Total) {
                    // 店舗IDと店舗名を渡す
                    val bundle = bundleOf("storeId" to total.id, "storeName" to total.name)
                    // 店舗ごとの支出リスト画面に遷移
                    findNavController().navigate(
                        R.id.action_navigation_stores_to_storeExpenseFragment,
                        bundle
                    )
                }
            }
        )

        val linearLayoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = totalListAdapter

        // セルの区切り線表示
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                view.context,
                linearLayoutManager.orientation
            )
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 支出リストの監視開始
        expensesViewModel.totalStoreList.observe(viewLifecycleOwner, { totalStoreList ->
            totalListAdapter.setTotalListItems(totalStoreList)
            // 総額を表示
            binding.allTotal.text = TextUtil.convertToStrWithCurrency(expensesViewModel.calcTotalStore())
        })
    }
}