let reverse = 0
let order_name = ['Сначала старые', 'Сначала новые']
let rows = []

function applyDateSmth() {
    let tBody = document.getElementById('1').querySelector('tbody')
    if (rows.length == 0) rows = Array.prototype.slice.call(tBody.querySelectorAll('tr'), 0)
    let start = document.getElementById('start').value
    let end = document.getElementById('end').value
    let dates = rows.map((row) =>
        new Date(Array.prototype.slice.call(row.childNodes, 0).at(-4).innerHTML))
    let merge = dates.map((date, i) => [date, rows[i]])
    merge.sort(function(a, b) {return a[0] - b[0]})
    if (reverse) merge = merge.reverse()
    let mask = new Array(rows.length).fill(1)
    for (let i = 0; i < dates.length; i++) {
        if (start != '') {
            if (merge[i][0].getTime() < new Date(start).getTime()) mask[i] = 0
        }
        if (end != '') {
            if (merge[i][0].getTime() > new Date(end).getTime()) mask[i] = 0
        }
    }
    tBody.innerHTML = ''
    for (let i = 0; i < rows.length; i++) {
        if (mask[i] == 1) tBody.appendChild(merge[i][1])
    }
}

document.getElementById('apply').addEventListener('click', applyDateSmth)

function changeOrder() {
    reverse = (reverse+1)%2
    document.getElementById('order').innerHTML = order_name[reverse]
}
document.getElementById('order').addEventListener('click', changeOrder)
