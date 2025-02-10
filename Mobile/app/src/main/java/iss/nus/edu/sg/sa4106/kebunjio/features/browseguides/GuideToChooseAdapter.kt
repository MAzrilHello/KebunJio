package iss.nus.edu.sg.sa4106.kebunjio.features.browseguides

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import iss.nus.edu.sg.sa4106.kebunjio.LoggedInFragment
import iss.nus.edu.sg.sa4106.kebunjio.R
import iss.nus.edu.sg.sa4106.kebunjio.data.EdiblePlantSpecies
import iss.nus.edu.sg.sa4106.kebunjio.databinding.ViewGuideToChooseBinding
class GuideToChooseAdapter(private val context: Context,
                           protected var loggedInFragment: LoggedInFragment,
                           protected var ediblePlantList: ArrayList<EdiblePlantSpecies>):
     ArrayAdapter<Any?>(context, R.layout.view_guide_to_choose) {
    init {
        addAll(*arrayOfNulls<Any>(ediblePlantList.size))
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var _view = view
        val binding: ViewGuideToChooseBinding

        if (_view == null) {
            val inflater = context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            // if we are not responsible for adding the view to the parent,
            // then attachToRoot should be 'false' (which is in our case)
            //_view = inflater.inflate(R.layout.view_plant_to_choose, parent, false)
            binding = ViewGuideToChooseBinding.inflate(inflater,parent,false)
            _view = binding.root
        } else {
            binding = ViewGuideToChooseBinding.bind(_view)
        }

        if (position >= ediblePlantList.size) {
            _view.visibility = View.GONE
            return _view
        }

        val theSpecies = ediblePlantList[position]
        val whichPlantText = binding.whichPlantText
        val viewGuideBtn = binding.viewGuideBtn

        whichPlantText.text = theSpecies.name

        viewGuideBtn.setOnClickListener {
            val intent = Intent(getContext(), GuideActivity::class.java)
            intent.putExtra("ediblePlantSpecies", theSpecies)
            getContext().startActivity(intent)
        }

        return _view
    }
}