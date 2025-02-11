package iss.nus.edu.sg.sa4106.kebunjio.features.browseguides

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import iss.nus.edu.sg.sa4106.kebunjio.LoggedInFragment
import iss.nus.edu.sg.sa4106.kebunjio.R
import iss.nus.edu.sg.sa4106.kebunjio.data.EdiblePlantSpecies
import iss.nus.edu.sg.sa4106.kebunjio.databinding.FragmentChooseGuideToViewBinding

class ChooseGuideToViewFragment : Fragment() {

    private var _binding: FragmentChooseGuideToViewBinding? = null
    private val binding get() = _binding!!

    private var ediblePlantSpeciesList: ArrayList<EdiblePlantSpecies> = ArrayList()
    private var loggedInFragment: LoggedInFragment? = null

    lateinit var guideList: ListView
    private var guideToChooseAdapter: GuideToChooseAdapter? = null

    public fun loadNewData(loggedInFragment: LoggedInFragment) {
        this.loggedInFragment = loggedInFragment
        this.ediblePlantSpeciesList = loggedInFragment.speciesList
        Log.d("ChooseGuideToViewFragment","ediblePlantSpeciesList size: ${ediblePlantSpeciesList.size}")
        //reloadEdiblePlantSpeciesList()
    }

    private fun reloadEdiblePlantSpeciesList() {
        //if (guideToChooseAdapter != null) {
        //    actLogListAdapter!!.resetData
        //}
        //if (actLogListAdapter != null) {
        //    Log.d("ChooseGuideToViewFragment","Resizing begins ${userId}, ${usersActivityLogList.size}, ${this.plantIdToNameDict.size}")
        //    actLogListAdapter!!.resetData(this.userId,this.usersActivityLogList,this.plantIdToNameDict)
        //}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseGuideToViewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val guideToViewText = binding.guideToViewText
        guideToViewText.text = "Plant Guides"
        guideList = binding.guideList

        guideToChooseAdapter = GuideToChooseAdapter(requireContext(),
                                            loggedInFragment!!,
                                            ediblePlantSpeciesList)
        guideList.adapter = guideToChooseAdapter

    }

    companion object {

    }
}