package com.redmechax00.todolist.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.redmechax00.todolist.R
import com.redmechax00.todolist.recycler_view.holders.main.HolderBaseItem
import kotlin.math.abs

const val swipeTrigger = 0.5f

class MyItemTouchHelper internal constructor(
    private val context: Context,
    dragDirs: Int,
    swipeDirs: Int,
    private val goToMainFragment: GoToMainFragment
) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    interface GoToMainFragment {
        fun onDrag(fromIndex: Int, toIndex: Int)
        fun onSwiped(direction: Int, holder: ViewHolder)
        fun onActionsEnd()
    }

    private val swipeThreshold = 1f


    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView, holder: ViewHolder
    ): Int {
        return when (holder) {
            is HolderBaseItem -> {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                var swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
                //If item is already checked, don`t run action Swipe
                if (holder.getCheckBoxState()) {
                    swipeFlags = ItemTouchHelper.START
                }
                return makeMovementFlags(dragFlags, swipeFlags)
            }
            else -> {
                0
            }
        }
    }

    override fun onMove(
        recyclerView: RecyclerView, holder: ViewHolder, target: ViewHolder
    ): Boolean {
        goToMainFragment.onDrag(
            holder.absoluteAdapterPosition, target.absoluteAdapterPosition
        )
        return true
    }

    override fun onSwiped(holder: ViewHolder, direction: Int) {
        goToMainFragment.onSwiped(direction, holder)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        holder: ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        //Drag
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            super.onChildDraw(c, recyclerView, holder, dX, dY, actionState, isCurrentlyActive)
        }

        //Swipe
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            holder as HolderBaseItem
            val itemView = holder.itemView

            //Draw background
            val position = holder.absoluteAdapterPosition

            if ((position != -1) && itemView.visibility != View.GONE) {
                drawDynamicBackground(c, recyclerView, itemView, dX)
            }

            //Draw item
            getDefaultUIUtil().onDraw(
                c, recyclerView, itemView, dX, dY, actionState, isCurrentlyActive
            )

        }

        super.onChildDraw(
            c,
            recyclerView,
            holder,
            dX * swipeThreshold,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        holder: ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDrawOver(c, recyclerView, holder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun clearView(recyclerView: RecyclerView, holder: ViewHolder) {
        super.clearView(recyclerView, holder)

        goToMainFragment.onActionsEnd()
    }

    private fun drawDynamicBackground(
        canvas: Canvas, recyclerView: RecyclerView, view: View, dX: Float
    ) {
        val direction = (dX / abs(dX)).toInt()
        val viewWidth = view.width
        val viewHeight = view.height
        val x0 = viewWidth * (-direction + 1) / 2f
        val y0 = calculateItemY(recyclerView, view)

        val iconSize: Int = context.resources.getDimension(R.dimen.image_size)
            .toInt() - context.resources.getDimension(R.dimen.image_padding).toInt() * 2
        val iconCenter = iconSize / 2f
        val icon: Bitmap?

        val marginX = iconSize.toFloat()
        val drawX = x0 - marginX * direction + (dX * swipeThreshold)
        val drawY = viewHeight * 0.5f

        val startDrawCircle: Float = (swipeTrigger * viewWidth) * 0.5f + marginX

        val someComputationalError = 1.025f
        val radius = (abs(dX) - startDrawCircle) * 2f *
                (swipeTrigger * viewWidth) * 0.5f /
                (swipeTrigger * viewWidth - ((swipeTrigger * viewWidth) * 0.5f + marginX / 2f)) *
                someComputationalError

        val bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888)
        val myCanvas = Canvas(bitmap)
        val paint = Paint()

        when (direction) {
            //Swipe to Delete
            -1 -> {
                paint.color = ContextCompat.getColor(context, R.color.color_action_delete)
                icon = getDrawableBitmap(R.drawable.ic_delete, iconSize)

                //Draw dynamic circle
                myCanvas.drawCircle(drawX, drawY, radius, paint)

                //Draw action icon
                icon?.let {
                    myCanvas.drawBitmap(icon, drawX - iconCenter, drawY - iconCenter, null)
                }
            }

            //Swipe to Success
            1 -> {
                paint.color = ContextCompat.getColor(context, R.color.color_action_success)
                icon = getDrawableBitmap(R.drawable.ic_success, iconSize)

                //Draw dynamic circle
                myCanvas.drawCircle(drawX, drawY, radius, paint)

                //Draw action icon
                icon?.let {
                    myCanvas.drawBitmap(icon, drawX - iconCenter, drawY - iconCenter, null)
                }
            }

            //Stop swipe
            0 -> {

            }
        }

        canvas.drawBitmap(bitmap, 0f, y0, null)
    }

    private fun calculateItemY(recyclerView: RecyclerView, view: View): Float {
        val intArrayXY = IntArray(2)
        view.getLocationInWindow(intArrayXY)
        val viewAbsY = intArrayXY[1]
        recyclerView.getLocationInWindow(intArrayXY)
        val recyclerViewAbsY = intArrayXY[1]
        return (viewAbsY - recyclerViewAbsY).toFloat()
    }

    private fun getDrawableBitmap(drawable: Int, iconSize: Int): Bitmap? {
        val iconDrawable: Drawable? =
            VectorDrawableCompat.create(context.resources, drawable, context.theme)
        return iconDrawable?.toBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888)
    }
}