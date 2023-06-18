package br.edu.scl.ifsp.ads.sharedlist.adapter

interface OnTaskClickListener {
    fun onTileTaskClick(position : Int)

    fun onEditMenuIconClick(position : Int)

    fun onRemoveMenuItemClick(position : Int)
}