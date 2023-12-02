let reverse = 0
let rows = []

function changeView() {
    let type = document.getElementById('view').value
    if (type == 'chart') {
        document.getElementById('1').style.display = 'None'
        let canvas = document.getElementById("canvas")
        canvas.style.display = 'block'
        let tBody = document.getElementById('1').querySelector('tbody')
        let rows_cur = Array.prototype.slice.call(tBody.querySelectorAll('tr'), 0)
        let dates = rows_cur.map((row) =>
            Array.prototype.slice.call(row.childNodes, 0).at(-4).innerHTML)
        let counter = new Map()
        for (let date of dates) {
            if (counter.get(date) == undefined) counter.set(date, 0)
            counter.set(date, counter.get(date)+1)
        }
        let border = 0.05
        let interval = canvas.width * (1 - border*2) / (counter.size + 1)
        let points = new Array(counter.size).fill(canvas.width * border + interval)
        for (let i = 1; i < points.length; i++) {
            points[i] = points[i-1] + interval
        }
        let heights = Array.from(counter.values())
        let labels = Array.from(counter.keys())
        let max_height = Math.max(...heights)
        let min_height = 50
        let segment_size = (canvas.height - min_height*2) / max_height
        let ctx = canvas.getContext('2d')
        ctx.clearRect(0,0, canvas.width, canvas.height)
        ctx.lineWidth = 40
        ctx.font = '15px Verdana'
        for (let i = 0; i < points.length; i++) {
            let x = points[i]
            let y_0 = canvas.height - min_height
            let y_1 = canvas.height - segment_size * heights[i] - min_height
            let textWidth1 = ctx.measureText(labels[i]).width
            let textWidth2 = ctx.measureText(heights[i]).width
            ctx.fillText(labels[i], x - textWidth1/2, y_0 + 30)
            ctx.fillText(heights[i], x - textWidth2/2, y_1 - 10)
            ctx.beginPath()
            ctx.moveTo(x, y_0)
            ctx.lineTo(x, y_1)
            ctx.stroke()
        }
    }

    else {
        document.getElementById('1').style.display = ''
        document.getElementById("canvas").style.display = 'None'
    }
}

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
    changeView()
    updateAmount()
}

function updateAmount() {
    let tBody = document.getElementById('1').querySelector('tbody')
    let rows_amount = Array.prototype.slice.call(tBody.querySelectorAll('tr'), 0)
    document.getElementById('amount').innerHTML = '' + rows_amount.length
}

window.onload = updateAmount

document.getElementById('apply').addEventListener('click', applyDateSmth)

function changeOrder() {
    reverse = (reverse+1)%2

}

